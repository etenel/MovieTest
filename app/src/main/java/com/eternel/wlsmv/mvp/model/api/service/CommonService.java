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

import com.eternel.wlsmv.mvp.model.api.Api;
import com.eternel.wlsmv.mvp.model.entity.ImageEntity;

import io.reactivex.Observable;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * ================================================
 * 存放通用的一些 API
 * <p>
 * Created by JessYan on 08/05/2016 12:05
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface CommonService {
    @Headers({RetrofitUrlManager.DOMAIN_NAME_HEADER + Api.Tencent,
            "Content-Type:application/x-www-form-urlencoded","Content-Length:"})
    @POST("fcgi-bin/ptu/ptu_faceage")
    Observable<ImageEntity> getBeautyValue(@Query("app_id") String app_id, @Query("time_stamp") String time_stamp,
                                           @Query("nonce_str") String nonce_str,
                                           @Query("sign") String sign,
                                           @Query("image") String image);

}
