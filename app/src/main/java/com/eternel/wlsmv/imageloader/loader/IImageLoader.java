package com.eternel.wlsmv.imageloader.loader;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.eternel.wlsmv.imageloader.glide.processlistener.OnProgressListener;

/**
 * Created by eternel
 * on 2018/4/5.
 */

public interface IImageLoader {
    //    无占位图
    void loadImage(Context context, String imageUrl, View imageView);

    //    有占位图
    void loadImage(Context context, String imageUrl, int placeholder, ImageView imageView);

    void loadGifImage(Context context, String imageUrl, int placeHolder, ImageView imageView);

    void loadGifImage(Context context, String imageUrl, ImageView imageView);

    //    先加载缩略图
    void loadThumbImage(Context context, String url, String thumbUrl, ImageView imageView);
//    加载成圆形
    void loadCircleImage(Context context, String url, int placeholder, ImageView imageView);

    void loadCircleBorderImage(Context context, String url, int placeholder, ImageView imageView, float borderWidth, int borderColor);

    void loadCircleBorderImage(Context context, String url, int placeholder, ImageView imageView, float borderWidth, int borderColor, int heightPx, int widthPx);

    void loadImageWithProgress(Context context, String url, ImageView imageView, OnProgressListener listener);

    void loadImageWithPrepareCall(Context context, String url, ImageView imageView, int placeholder, SourceReadyListener listener);

    void loadGifWithPrepareCall(Context context, String url, ImageView imageView, SourceReadyListener listener);

    //清除硬盘缓存
    void clearImageDiskCache(final Context context);

    //清除内存缓存
    void clearImageMemoryCache(Context context);

    //根据不同的内存状态，来响应不同的内存释放策略
    void trimMemory(Context context, int level);

    //获取缓存大小
    String getCacheSize(Context context);

    void saveImage(Context context, String url, String savePath, String saveFileName, ImageSaveListener listener);

}
