package com.eternel.wlsmv.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eternel.wlsmv.app.utils.RxUtils;
import com.eternel.wlsmv.mvp.model.entity.ImageEntity;
import com.eternel.wlsmv.mvp.ui.adapter.ImageListAdapter;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.eternel.wlsmv.mvp.contract.ImageContract;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;

import static com.jess.arms.utils.ArmsUtils.startActivity;


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
    private int post_id;
    private boolean isFirst = true;
    private int pager = 1;
    private String type;

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

    public void initListener() {
        imageListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(imageListAdapter.getItem(position).getUrl()));
                startActivity(intent);
            }
        });
        imageListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(imageListAdapter.getItem(position).getUrl()));
                startActivity(intent);
            }
        });
    }

    ;

    public void getImages(boolean refresh) {
        if (refresh) {
            pager = 1;
            type = "refresh";
        } else {
            pager=pager+1;
            type = "loadmore";
        }

        boolean isEvictCache = refresh;//是否驱逐缓存,为ture即不使用缓存,每次下拉刷新即需要最新数据,则不使用缓存
        if (refresh && isFirst) {//默认在第一次下拉刷新时使用缓存
            isFirst = false;
            isEvictCache = false;
        }
        if (refresh) {
            mModel.getImages(pager, isEvictCache, type)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable -> {
                        mRootView.showLoading();
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            mRootView.hideLoading();
                        }
                    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                    .subscribe(new ErrorHandleSubscriber<ImageEntity>(mErrorHandler) {
                        @Override
                        public void onNext(ImageEntity imageEntity) {
                            images = imageEntity.getFeedList();
                            if (images.size() > 0) {
                                post_id = images.get(images.size() - 1).getPost_id();//记录最后一个id,用于下一次请求
                            }
                            imageListAdapter.setNewData(images);
                        }
                    });
        } else {
            mModel.getMoreImages(pager, true, type, post_id)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable -> {
                        mRootView.startLoadMore();
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            mRootView.endLoadMore();
                        }
                    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                    .subscribe(new ErrorHandleSubscriber<ImageEntity>(mErrorHandler) {
                        @Override
                        public void onNext(ImageEntity imageEntity) {
                            images = imageEntity.getFeedList();
                            if (images.size() > 0) {
                                post_id = images.get(images.size() - 1).getPost_id();//记录最后一个id,用于下一次请求
                            }
                            imageListAdapter.addData(images);
                        }
                    });
        }

    }
}
