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
package com.eternel.wlsmv.mvp.model.api.service;

import com.eternel.wlsmv.mvp.model.entity.ImageEntity;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * ================================================
 * 展示 {@link Retrofit#create(Class)} 中需要传入的 ApiService 的使用方式
 * 存放关于用户的一些 API
 * <p>
 * Created by JessYan on 08/05/2016 12:05
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface UserService {
    //String HEADER_API_VERSION = "Accept: application/vnd.github.v3+json";
    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,如是否使用缓存
//    page：翻页值。从 1 开始。需要搭配 json 中的 pose_id 字段使用。可为空
//    post_id：如果是第一页则不需要添加该字段。否则，需要加上该字段，该字段的值为上一页最后一个 json 中的 post_id 值
//    type：如果是第一页则是 refresh，如果是加载更多，则是 loadmore。可为空
    @GET("feed-app")
    Observable<ImageEntity> getFirstMoviesList(@Query("page") int page, @Query("type") String type);

    @GET("feed-app")
    Observable<ImageEntity> getMoviesList(@Query("page") int page, @Query("post_id") int post_id, @Query("type") String type);

    @GET("feed-app")
    Observable<ImageEntity> getImages(@Query("page") int page, @Query("type") String type);
    @GET("feed-app")
    Observable<ImageEntity> getMoreImages(@Query("page") int page, @Query("type") String type,@Query("post_id") int post_id);
}
