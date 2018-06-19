package com.eternel.wlsmv.mvp.presenter;

import android.app.Application;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.eternel.wlsmv.constant.DouBanConstant;
import com.eternel.wlsmv.mvp.model.entity.BaseJson;
import com.eternel.wlsmv.mvp.model.entity.MoviesEntity;
import com.eternel.wlsmv.mvp.ui.adapter.MovieListAdapter;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.eternel.wlsmv.mvp.contract.CurrentMovieContract;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;


@ActivityScope
public class CurrentMoviePresenter extends BasePresenter<CurrentMovieContract.Model, CurrentMovieContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    MovieListAdapter movieListAdapter;
    private int start = 0;


    @Inject
    public CurrentMoviePresenter(CurrentMovieContract.Model model, CurrentMovieContract.View rootView) {
        super(model, rootView);

    }

    public void getMovies(boolean refresh) {
        mModel.getdata().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<BaseJson>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson baseJson) {

                    }
                });
        if (refresh) {
            start = 0;
        } else {
            start++;
        }
        String city = SPUtils.getInstance().getString("city", "北京");
//        mModel.getMovies(DouBanConstant.apikey, city, start, DouBanConstant.count, DouBanConstant.udid, DouBanConstant.client)
//                .subscribeOn(Schedulers.io())
//                .doFinally(() -> {
//                    if (refresh) {
//                        mRootView.hideLoading();
//                    } else {
//                        mRootView.hideLoadMore();
//
//                    }
//                }).subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
//                .subscribe(new ErrorHandleSubscriber<MoviesEntity>(mErrorHandler) {
//                    @Override
//                    public void onNext(MoviesEntity moviesEntity) {
//                        List<MoviesEntity.SubjectsBean> subjects = moviesEntity.getSubjects();
//                        mRootView.setTitle(moviesEntity.getTitle());
//                        if (refresh) {
//                            movieListAdapter.setNewData(subjects);
//                        } else {
//                            movieListAdapter.addData(subjects);
//                        }
//                    }
//                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        this.movieListAdapter = null;
    }
}
