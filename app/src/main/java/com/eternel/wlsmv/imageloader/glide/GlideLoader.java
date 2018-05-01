package com.eternel.wlsmv.imageloader.glide;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.eternel.wlsmv.imageloader.glide.processlistener.OnProgressListener;
import com.eternel.wlsmv.imageloader.glide.processlistener.ProgressManager;
import com.eternel.wlsmv.imageloader.loader.CommonUtils;
import com.eternel.wlsmv.imageloader.loader.IImageLoader;
import com.eternel.wlsmv.imageloader.loader.ImageSaveListener;
import com.eternel.wlsmv.imageloader.loader.SourceReadyListener;
import com.jess.arms.http.imageloader.glide.GlideArms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by eternel
 * on 2018/4/5.
 */

public class GlideLoader implements IImageLoader {
    private Handler mMainThreadHandler;
    private Object mImageUrlObj;
    private long mTotalBytes = 0;
    private long mLastBytesRead = 0;
    private boolean mLastStatus = false;
    private OnProgressListener internalProgressListener;
    private OnGlideImageViewListener onGlideImageViewListener;
    private OnProgressListener onProgressListener;

    @Override
    public void loadImage(Context context, String imageUrl, View imageView) {
        GlideArms.with(context).load(imageUrl).into((ImageView) imageView);
    }

    @Override
    public void loadImage(Context context, String imageUrl, int placeholder, ImageView imageView) {
        GlideArms.with(context).load(imageUrl).placeholder(placeholder).into(imageView);
    }

    @Override
    public void loadGifImage(Context context, String imageUrl, int placeHolder, ImageView imageView) {
        GlideArms.with(context).load(imageUrl).placeholder(placeHolder).into(imageView);
    }

    @Override
    public void loadGifImage(Context context, String imageUrl, ImageView imageView) {
        GlideArms.with(context).load(imageUrl).into(imageView);
    }

    @Override
    public void loadThumbImage(Context context, String url, String thumbUrl, ImageView imageView) {
        GlideArms.with(context).load(url).thumbnail(GlideArms.with(context).load(thumbUrl)).into(imageView);
    }

    @Override
    public void loadCircleImage(Context context, String url, int placeholder, ImageView imageView) {
        GlideArms.with(context).load(url).circleCrop().into(imageView);
    }

    @Override
    public void loadCircleBorderImage(Context context, String url, int placeholder, ImageView imageView, float borderWidth, int borderColor) {

        GlideArms.with(context).load(url).placeholder(placeholder).transform(new GlideCircleTransform(context, borderWidth, borderColor)).into(imageView);
    }

    @Override
    public void loadCircleBorderImage(Context context, String url, int placeholder, ImageView imageView, float borderWidth, int borderColor, int heightPx, int widthPx) {
        GlideArms.with(context).load(url).placeholder(placeholder).transform(new GlideCircleTransform(context, borderWidth, borderColor, heightPx, widthPx)).into(imageView);

    }
    @Override
    public void loadImageWithProgress(Context context, String url, ImageView imageView, OnProgressListener listener) {
        mMainThreadHandler = new Handler(Looper.getMainLooper());
        setOnProgressListener(url,listener);
        GlideArms.with(context).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                mainThreadCallback(mLastBytesRead, mTotalBytes, true, e);
                ProgressManager.removeProgressListener(internalProgressListener);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                mainThreadCallback(mLastBytesRead, mTotalBytes, true, null);
                ProgressManager.removeProgressListener(internalProgressListener);
                return false;
            }
        }).into(imageView);
    }

    private void mainThreadCallback(final long bytesRead, final long totalBytes, final boolean isDone, final GlideException exception) {
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                final int percent = (int) ((bytesRead * 1.0f / totalBytes) * 100.0f);
                if (onProgressListener != null) {
                    onProgressListener.onProgress((String) mImageUrlObj, bytesRead, totalBytes, isDone, exception);
                }

                if (onGlideImageViewListener != null) {
                    onGlideImageViewListener.onProgress(percent, isDone, exception);
                }
            }
        });
    }

    private void addProgressListener() {
        internalProgressListener = new OnProgressListener() {
            @Override
            public void onProgress(String imageUrl, long bytesRead, long totalBytes, boolean isDone, GlideException exception) {
                if (totalBytes == 0) return;
                if (mLastBytesRead == bytesRead && mLastStatus == isDone) return;
                mLastBytesRead = bytesRead;
                mTotalBytes = totalBytes;
                mLastStatus = isDone;
                mainThreadCallback(bytesRead, totalBytes, isDone, exception);
                if (isDone) {
                    ProgressManager.removeProgressListener(this);
                }
            }
        };
        ProgressManager.addProgressListener(internalProgressListener);
    }

    public void setOnGlideImageViewListener(String imageUrl, OnGlideImageViewListener onGlideImageViewListener) {
        this.mImageUrlObj = imageUrl;
        this.onGlideImageViewListener = onGlideImageViewListener;
        addProgressListener();
    }

    public void setOnProgressListener(String imageUrl, OnProgressListener onProgressListener) {
        this.mImageUrlObj = imageUrl;
        this.onProgressListener = onProgressListener;
        addProgressListener();
    }

    @Override
    public void loadImageWithPrepareCall(Context context, String url, ImageView imageView, int placeholder, SourceReadyListener listener) {

    }

    @Override
    public void loadGifWithPrepareCall(Context context, String url, ImageView imageView, SourceReadyListener listener) {

    }
    //清除硬盘缓存
    @Override
    public void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GlideArms.get(context.getApplicationContext()).clearDiskCache();
                    }
                }).start();
            } else {
                GlideArms.get(context.getApplicationContext()).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //清除内存缓存
    @Override
    public void clearImageMemoryCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper())
                GlideArms.get(context.getApplicationContext()).clearMemory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //根据不同的内存状态，来响应不同的内存释放策略
    @Override
    public void trimMemory(Context context, int level) {
        GlideArms.get(context).trimMemory(level);
    }

    @Override
    public String getCacheSize(Context context) {
        return null;
    }

    @Override
    public void saveImage(Context context, String url, String savePath, String saveFileName, ImageSaveListener listener) {
        if (!CommonUtils.isSDCardExsit() || TextUtils.isEmpty(url)) {
            listener.onSaveFail();
            return;
        }
        InputStream fromStream = null;
        OutputStream toStream = null;
        try {
            File cacheFile = Glide.with(context).load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            if (cacheFile == null || !cacheFile.exists()) {
                listener.onSaveFail();
                return;
            }
            File dir = new File(savePath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir, saveFileName + CommonUtils.getPicType(cacheFile.getAbsolutePath()));

            fromStream = new FileInputStream(cacheFile);
            toStream = new FileOutputStream(file);
            byte length[] = new byte[1024];
            int count;
            while ((count = fromStream.read(length)) > 0) {
                toStream.write(length, 0, count);
            }
            //用广播通知相册进行更新相册
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            context.sendBroadcast(intent);
            listener.onSaveSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            listener.onSaveFail();
        } finally {
            if (fromStream != null) {
                try {
                    fromStream.close();
                    toStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    fromStream = null;
                    toStream = null;
                }
            }
        }
    }


}
