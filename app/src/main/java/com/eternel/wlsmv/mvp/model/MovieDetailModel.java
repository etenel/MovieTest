package com.eternel.wlsmv.mvp.model;

import android.app.Application;

import com.eternel.wlsmv.mvp.model.api.service.DouBanService;
import com.eternel.wlsmv.mvp.model.entity.MovieDetailEntity;
import com.eternel.wlsmv.mvp.model.entity.MoviesEntity;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.eternel.wlsmv.mvp.contract.MovieDetailContract;

import io.reactivex.Observable;


@ActivityScope
public class MovieDetailModel extends BaseModel implements MovieDetailContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public MovieDetailModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<MovieDetailEntity> getMovieDetail(String id, String apikey, String city, String udid, String client) {
        return mRepositoryManager.obtainRetrofitService(DouBanService.class)
                .getMovieDetail(id, apikey, city, udid, client);
    }
}