package com.eternel.wlsmv.mvp.model.api.service;

import com.eternel.wlsmv.mvp.model.api.Api;
import com.eternel.wlsmv.mvp.model.entity.BaseJson;
import com.eternel.wlsmv.mvp.model.entity.ImageEntity;
import com.eternel.wlsmv.mvp.model.entity.MovieDetailEntity;
import com.eternel.wlsmv.mvp.model.entity.MoviesEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.eternel.wlsmv.mvp.model.api.Api.DOUBAN_DOMAIN_NAME;
import static com.eternel.wlsmv.mvp.model.api.Api.t;
import static me.jessyan.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER;

/**
 * Created by etenel
 * on 2018/5/28.
 */

public interface DouBanService {
    //    apikey：固定值0b2bdeda43b5688921839c8ecb20399b
//    city：所在城市，例如北京、上海等
//    start：分页使用，表示第几页
//    count：分页使用，表示数量
//    client：客户端信息。可为空
//    udid：用户 id。可为空
//    https://api.douban.com/v2/movie/in_theaters?apikey=0b2bdeda43b5688921839c8ecb20399b&city=%E5%8C%97%E4%BA%AC&start=0&count=10&client=somemessage&udid=dddddddddddddddddddddd
    @Headers(DOMAIN_NAME_HEADER + DOUBAN_DOMAIN_NAME)
    @GET("v2/movie/in_theaters")
    Observable<MoviesEntity> getMovies(@Query("apikey") String apikey, @Query("city") String city,
                                       @Query("start") int start, @Query("count") int count, @Query("udid") String udid, @Query("client") String client);

    @Headers(DOMAIN_NAME_HEADER + DOUBAN_DOMAIN_NAME)
    @GET("v2/movie/subject/{id}")
    Observable<MovieDetailEntity> getMovieDetail(@Path("id") String id, @Query("apikey") String apikey, @Query("city") String city,
                                                 @Query("udid") String udid, @Query("client") String client);

    @Headers(DOMAIN_NAME_HEADER + t)
    @POST("authentication")
    Observable<BaseJson> getdata();
}
