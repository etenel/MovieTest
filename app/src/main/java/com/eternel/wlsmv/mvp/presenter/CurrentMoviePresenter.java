package com.eternel.wlsmv.mvp.presenter;

import android.app.Application;

import com.blankj.utilcode.util.LogUtils;
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
    private int count = 100;
    private String apikey = "0b2bdeda43b5688921839c8ecb20399b";
    private String client = "message";
    private String udid = "sffdaetgreytadvhdy";

    @Inject
    public CurrentMoviePresenter(CurrentMovieContract.Model model, CurrentMovieContract.View rootView) {
        super(model, rootView);

    }

    public void getMovies(boolean refresh) {
        if (refresh) {
            start = 0;
        } else {
            start++;
        }
       LogUtils.e(start+"");
        mModel.getMovies(apikey, "北京", start, count, udid, client)
                .subscribeOn(Schedulers.io())
                .doFinally(() -> {
                    if (refresh) {
                        mRootView.hideLoading();
                    } else {
                        mRootView.hideLoadMore();

                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<MoviesEntity>(mErrorHandler) {
                    @Override
                    public void onNext(MoviesEntity moviesEntity) {
                        List<MoviesEntity.SubjectsBean> subjects = moviesEntity.getSubjects();
                        mRootView.setTitle(moviesEntity.getTitle());
                        if(refresh) {
                            movieListAdapter.setNewData(subjects);
                        }else{
                            movieListAdapter.addData(subjects);
                        }
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
        this.movieListAdapter=null;
    }
}
