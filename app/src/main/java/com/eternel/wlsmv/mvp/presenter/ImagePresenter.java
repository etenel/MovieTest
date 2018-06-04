package com.eternel.wlsmv.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eternel.wlsmv.R;
import com.eternel.wlsmv.mvp.model.entity.ImageEntity;
import com.eternel.wlsmv.mvp.model.entity.ImageTagsEntity;
import com.eternel.wlsmv.mvp.ui.activity.TagDetailActivity;
import com.eternel.wlsmv.mvp.ui.adapter.ImageListAdapter;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.eternel.wlsmv.mvp.contract.ImageContract;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;

import static com.jess.arms.utils.ArmsUtils.startActivity;
import static com.jess.arms.utils.ArmsUtils.statuInScreen;


@ActivityScope
public class ImagePresenter extends BasePresenter<ImageContract.Model, ImageContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    ImageListAdapter imageListAdapter;
    @Inject
    List<ImageEntity.FeedListBean> images;
    private int page = 1;
    private int count = 20;

    @Inject
    public ImagePresenter(ImageContract.Model model, ImageContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        this.images = null;
        this.imageListAdapter = null;
    }

    public void getTags(boolean refresh) {
        if (refresh) {
            page = 1;
        } else {
            page++;
        }
        String tag = SPUtils.getInstance(mApplication.getString(R.string.tags)).getString(mApplication.getString(R.string.tags), "subject");
        mModel.getTags(tag, page, count)
                .subscribeOn(Schedulers.io())
                .doFinally(() -> {
                    if (refresh) {
                        mRootView.hideLoading();
                    } else {
                        mRootView.endLoadMore();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<ImageTagsEntity>(mErrorHandler) {
                    @Override
                    public void onNext(ImageTagsEntity imageTagsEntity) {
                        if ("SUCCESS".equals(imageTagsEntity.getResult())) {
                            ImageTagsEntity.DataBean data = imageTagsEntity.getData();
                            List<ImageTagsEntity.DataBean.TagListBean> tag_list = data.getTag_list();
                            if (refresh) {
                                imageListAdapter.setNewData(tag_list);
                            } else {
                                imageListAdapter.addData(tag_list);
                            }
                        } else {
                            ToastUtils.showShort(imageTagsEntity.getResult() + imageTagsEntity.getMessage());
                        }
                    }
                });



    }
}
