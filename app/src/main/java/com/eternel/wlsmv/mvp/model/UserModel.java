/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eternel.wlsmv.mvp.model;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.eternel.wlsmv.mvp.model.entity.ImageEntity;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;

import com.eternel.wlsmv.mvp.contract.UserContract;
import com.eternel.wlsmv.mvp.model.api.cache.CommonCache;
import com.eternel.wlsmv.mvp.model.api.service.UserService;

import io.rx_cache2.Reply;
import timber.log.Timber;

/**
 * ================================================
 * 展示 Model 的用法
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#2.4.3">Model wiki 官方文档</a>
 * Created by JessYan on 09/04/2016 10:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
@ActivityScope
public class UserModel extends BaseModel implements UserContract.Model {
    public static final int USERS_PER_PAGE = 10;

    @Inject
    public UserModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<ImageEntity> getFirstMovies(int page, boolean update, String type) {
        //使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(UserService.class)
                .getFirstMoviesList(page, type))
                .flatMap(new Function<Observable<ImageEntity>, ObservableSource<ImageEntity>>() {
                    @Override
                    public ObservableSource<ImageEntity> apply(Observable<ImageEntity> movieEntityObservable) throws Exception {
                        return movieEntityObservable;
                    }
                });


    }

    @Override
    public Observable<ImageEntity> getMovies(int page, boolean update, String type, int post_id) {
        //使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(UserService.class)
                .getMoviesList(page, post_id, type))
                .flatMap(new Function<Observable<ImageEntity>, ObservableSource<ImageEntity>>() {
                    @Override
                    public ObservableSource<ImageEntity> apply(Observable<ImageEntity> movieEntityObservable) throws Exception {

                        return mRepositoryManager.obtainCacheService(CommonCache.class)
                                .getUsers(movieEntityObservable,
                                        new DynamicKey(page)
                                        , new EvictDynamicKey(update))
                                .map(Reply::getData);
                    }
                });


//                .flatMap(new Function<Observable<ImageEntity, ObservableSource<List<ImageEntity.FeedListBean>>>() {
//                    @Override
//                    public ObservableSource<ImageEntity> apply(@NonNull Observable<List<ImageEntity.FeedListBean>> listObservable) throws Exception {
//                        return mRepositoryManager.obtainCacheService(CommonCache.class)
//                                .getUsers(listObservable
//                                        , new DynamicKey(page)
//                                        , new EvictDynamicKey(update))
//                                .map(listReply -> listReply.getData());
//                    }
//                });

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        Timber.d("Release Resource");
    }


}
