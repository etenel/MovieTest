package com.eternel.wlsmv.mvp.model;

import android.app.Application;

import com.eternel.wlsmv.mvp.model.api.service.ImageService;

import com.eternel.wlsmv.mvp.model.entity.ImageTagsEntity;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.eternel.wlsmv.mvp.contract.ImageContract;

import io.reactivex.Observable;


@ActivityScope
public class ImageModel extends BaseModel implements ImageContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public ImageModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<ImageTagsEntity> getTags(String tag, int page, int count) {
        return mRepositoryManager.obtainRetrofitService(ImageService.class)
                .getTags(tag, page, count);
    }
}