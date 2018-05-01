package com.eternel.wlsmv.imageloader.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.module.LibraryGlideModule;
import com.eternel.wlsmv.imageloader.glide.processlistener.ProgressManager;
import com.jess.arms.http.imageloader.glide.GlideConfiguration;

import java.io.InputStream;

/**
 * Created by eternel
 * on 2018/4/5.
 */
@com.bumptech.glide.annotation.GlideModule(glideName = "GlideApp")
public final class GlideModule extends LibraryGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(ProgressManager.getOkHttpClient()));
    }
}
