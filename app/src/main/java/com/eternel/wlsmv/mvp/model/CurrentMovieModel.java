package com.eternel.wlsmv.mvp.model;

import android.app.Application;

import com.eternel.wlsmv.mvp.model.api.Api;
import com.eternel.wlsmv.mvp.model.api.service.DouBanService;
import com.eternel.wlsmv.mvp.model.entity.MoviesEntity;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.eternel.wlsmv.mvp.contract.CurrentMovieContract;

import io.reactivex.Observable;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;


@ActivityScope
public class CurrentMovieModel extends BaseModel implements CurrentMovieContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public CurrentMovieModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<MoviesEntity> getMovies(String apikey, String city, int start, int count, String udid, String client) {
        RetrofitUrlManager.getInstance().putDomain(Api.DOUBAN_DOMAIN_NAME, Api.APP_DOUBAN_DOMAIN);
        return mRepositoryManager.obtainRetrofitService(DouBanService.class)
                .getMovies(apikey, city, start, count, udid, client);
    }
}