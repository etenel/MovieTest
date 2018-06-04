package com.eternel.wlsmv.mvp.presenter;

import android.app.Application;

import com.blankj.utilcode.util.SPUtils;
import com.eternel.wlsmv.constant.DouBanConstant;
import com.eternel.wlsmv.mvp.model.entity.MovieDetailEntity;
import com.eternel.wlsmv.mvp.model.entity.MoviesEntity;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.eternel.wlsmv.mvp.contract.MovieDetailContract;
import com.jess.arms.utils.RxLifecycleUtils;


@ActivityScope
public class MovieDetailPresenter extends BasePresenter<MovieDetailContract.Model, MovieDetailContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public MovieDetailPresenter(MovieDetailContract.Model model, MovieDetailContract.View rootView) {
        super(model, rootView);
    }

    public void getMovieDetail(String id) {
        String city = SPUtils.getInstance().getString("city", "北京");
        mModel.getMovieDetail(id, DouBanConstant.apikey, city, DouBanConstant.udid, DouBanConstant.client)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<MovieDetailEntity>(mErrorHandler) {
                    @Override
                    public void onNext(MovieDetailEntity moviesEntity) {
                        mRootView.setDetail(moviesEntity);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

}
