package com.eternel.wlsmv.imageloader.loader;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.eternel.wlsmv.imageloader.glide.GlideLoader;
import com.eternel.wlsmv.imageloader.glide.processlistener.OnProgressListener;

/**
 * Created by eternel
 * on 2018/4/5.
 */

public class ImageLoader implements IImageLoader {
    private static IImageLoader imageLoader = new GlideLoader();
    private static ImageLoader loader;

    public static void init(IImageLoader loader) {
        imageLoader = loader;
    }

    public static ImageLoader getInstance() {
        if (loader == null) {
            loader = new ImageLoader();
            return loader;
        }
        return loader;
    }

    public ImageLoader exchange(IImageLoader loader) {
        imageLoader = loader;
        return this;
    }

    @Override
    public void loadImage(Context context, String imageUrl, View imageView) {
        imageLoader.loadImage(context, imageUrl, imageView);
    }

    @Override
    public void loadImage(Context context, String imageUrl, int placeholder, ImageView imageView) {
        imageLoader.loadImage(context, imageUrl, placeholder, imageView);
    }

    @Override
    public void loadGifImage(Context context, String imageUrl, int placeHolder, ImageView imageView) {
        imageLoader.loadGifImage(context, imageUrl, placeHolder, imageView);
    }

    @Override
    public void loadGifImage(Context context, String imageUrl, ImageView imageView) {
        imageLoader.loadGifImage(context, imageUrl, imageView);
    }

    @Override
    public void loadThumbImage(Context context, String url, String thumbUrl, ImageView imageView) {
        imageLoader.loadThumbImage(context, url, thumbUrl, imageView);
    }

    @Override
    public void loadCircleImage(Context context, String url, int placeholder, ImageView imageView) {
        imageLoader.loadCircleImage(context, url, placeholder, imageView);
    }

    @Override
    public void loadCircleBorderImage(Context context, String url, int placeholder, ImageView imageView, float borderWidth, int borderColor) {
        imageLoader.loadCircleBorderImage(context, url, placeholder, imageView, borderWidth, borderColor);
    }

    @Override
    public void loadCircleBorderImage(Context context, String url, int placeholder, ImageView imageView, float borderWidth, int borderColor, int heightPx, int widthPx) {
        imageLoader.loadCircleBorderImage(context, url, placeholder, imageView, borderWidth, borderColor, heightPx, widthPx);

    }

    @Override
    public void loadImageWithProgress(Context context, String url, ImageView imageView, OnProgressListener listener) {
        imageLoader.loadImageWithProgress(context, url, imageView, listener);
    }

    @Override
    public void loadImageWithPrepareCall(Context context, String url, ImageView imageView, int placeholder, SourceReadyListener listener) {
        imageLoader.loadImageWithPrepareCall(context, url, imageView, placeholder, listener);
    }

    @Override
    public void loadGifWithPrepareCall(Context context, String url, ImageView imageView, SourceReadyListener listener) {
        imageLoader.loadGifWithPrepareCall(context, url, imageView, listener);
    }

    @Override
    public void clearImageDiskCache(Context context) {
        imageLoader.clearImageDiskCache(context);
    }

    @Override
    public void clearImageMemoryCache(Context context) {
        imageLoader.clearImageMemoryCache(context);
    }

    @Override
    public void trimMemory(Context context, int level) {
        imageLoader.trimMemory(context, level);
    }

    @Override
    public String getCacheSize(Context context) {
        return imageLoader.getCacheSize(context);
    }

    @Override
    public void saveImage(Context context, String url, String savePath, String saveFileName, ImageSaveListener listener) {
        imageLoader.saveImage(context, url, savePath, saveFileName, listener);
    }
}
