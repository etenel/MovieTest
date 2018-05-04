package com.eternel.wlsmv.mvp.model.api.service;

import com.eternel.wlsmv.mvp.model.entity.ImageEntity;
import com.eternel.wlsmv.mvp.model.entity.ImageTagsEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by etenel
 * on 2018/5/4.
 */

public interface ImageService {
    /**
     * 获取图虫各种标签列表
     * @param tag
     * @param page
     * @param count
     * @return
     */
    @GET("rest/tag-categories/{tag}")
    Observable<ImageTagsEntity> getTags(@Path("tag") String tag, @Query("page") int page, @Query("count") int count);
}
