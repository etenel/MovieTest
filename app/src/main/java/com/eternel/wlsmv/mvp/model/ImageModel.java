package com.eternel.wlsmv.mvp.model;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.eternel.wlsmv.mvp.model.api.cache.CommonCache;
import com.eternel.wlsmv.mvp.model.api.service.UserService;
import com.eternel.wlsmv.mvp.model.entity.ImageEntity;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.eternel.wlsmv.mvp.contract.ImageContract;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.Reply;
import timber.log.Timber;


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
    public Observable<ImageEntity> getImages(int page, boolean refresh, String type) {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(UserService.class)
                .getImages(page, type))
                .flatMap(imageEntityObservable -> mRepositoryManager.obtainCacheService(CommonCache.class)
                        .getCacheImages(imageEntityObservable, new DynamicKey(page), new EvictDynamicKey(refresh))
                        .map(Reply::getData));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        Timber.d("pause:release");
    }

    @Override
    public Observable<ImageEntity> getMoreImages(int page, boolean refresh, String type, int post_id) {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(UserService.class)
                .getMoreImages(page, type,post_id))
                .flatMap(imageEntityObservable -> mRepositoryManager.obtainCacheService(CommonCache.class)
                        .getCacheImages(imageEntityObservable, new DynamicKey(page), new EvictDynamicKey(refresh))
                        .map(Reply::getData));
    }

    ;
}