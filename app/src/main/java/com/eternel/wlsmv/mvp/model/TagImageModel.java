package com.eternel.wlsmv.mvp.model;

import android.app.Application;

import com.eternel.wlsmv.mvp.model.api.service.ImageService;
import com.eternel.wlsmv.mvp.model.entity.TagDetailListEntity;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.FragmentScope;

import javax.inject.Inject;

import com.eternel.wlsmv.mvp.contract.TagImageContract;

import io.reactivex.Observable;


@FragmentScope
public class TagImageModel extends BaseModel implements TagImageContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public TagImageModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<TagDetailListEntity> getTagDetailList(String tag, int page, int count, String order) {
        return mRepositoryManager.obtainRetrofitService(ImageService.class)
                .getTagDetailList(tag, page, count, order);
    }
}