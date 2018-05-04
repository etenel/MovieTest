package com.eternel.wlsmv.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.eternel.wlsmv.mvp.ui.adapter.TagImageListAdapter;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.eternel.wlsmv.mvp.contract.TagImageContract;
import com.jess.arms.utils.RxLifecycleUtils;


@FragmentScope
public class TagImagePresenter extends BasePresenter<TagImageContract.Model, TagImageContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    TagImageListAdapter imageListAdapter;
    private int page = 1;
    private int count = 20;
    private String tag;
    private String order;

    @Inject
    public TagImagePresenter(TagImageContract.Model model, TagImageContract.View rootView) {
        super(model, rootView);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        this.imageListAdapter = null;
    }

    public void getDatas(boolean refresh) {
        tag = mRootView.getTagName();
        order = mRootView.getOrder();
        if (refresh) {
            page = 1;
        } else {
            page++;
        }
        mModel.getTagDetailList(tag, page, count, order)
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
                .subscribe(tagDetailListEntity -> {
                    if (refresh) {
                        imageListAdapter.setNewData(tagDetailListEntity.getPostList());
                    } else {
                        imageListAdapter.addData(tagDetailListEntity.getPostList());
                    }
                });
    }
}
