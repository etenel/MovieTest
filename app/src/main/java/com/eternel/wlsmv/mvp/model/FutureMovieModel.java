package com.eternel.wlsmv.mvp.model;

import android.app.Application;
import android.util.Base64;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.eternel.wlsmv.app.utils.HttpsUtil4Tencent;
import com.eternel.wlsmv.mvp.model.api.Api;
import com.eternel.wlsmv.mvp.model.api.service.CommonService;
import com.eternel.wlsmv.mvp.model.entity.ImageEntity;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.eternel.wlsmv.mvp.contract.FutureMovieContract;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


@ActivityScope
public class FutureMovieModel extends BaseModel implements FutureMovieContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public FutureMovieModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public String getBeautyValue(String url, Map<String, String> headers, Map<String, String> content) {

        try {
            HttpResponse httpResponse = HttpsUtil4Tencent.doPostTencentAI(url, headers, content);
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "null";
    }


}