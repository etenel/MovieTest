package com.eternel.wlsmv.mvp.presenter;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.eternel.wlsmv.R;
import com.eternel.wlsmv.app.utils.Base64Util;
import com.eternel.wlsmv.app.utils.HttpsUtil4Tencent;
import com.eternel.wlsmv.app.utils.MD5;
import com.eternel.wlsmv.app.utils.TencentAISignSort;
import com.eternel.wlsmv.constant.TencentConstant;
import com.eternel.wlsmv.mvp.model.api.Api;
import com.eternel.wlsmv.mvp.model.entity.ImageEntity;
import com.eternel.wlsmv.mvp.model.entity.TencentAI;
import com.google.gson.Gson;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import top.zibin.luban.Luban;

import javax.inject.Inject;

import com.eternel.wlsmv.mvp.contract.FutureMovieContract;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.reactivestreams.Subscription;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;


@ActivityScope
public class FutureMoviePresenter extends BasePresenter<FutureMovieContract.Model, FutureMovieContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    private String photofileName;
    private Uri imagUrl;

    @Inject
    public FutureMoviePresenter(FutureMovieContract.Model model, FutureMovieContract.View rootView) {
        super(model, rootView);
    }

    @SuppressLint("CheckResult")
    public void addImage(Uri image) {
        File file = getFileByUri(image);
        if (file == null) {
            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/images/" + photofileName);
            file = FileUtils.getFileByPath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/images/" + photofileName);
            bitmap = null;
        }
        if (file != null && file.exists()) {
            LogUtils.e(file.getPath());
            File finalFile = file;
            Flowable.just(file)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(subscription -> {
                        mRootView.showLoading();
                        mRootView.loadImage(finalFile);
                    })
                    .doFinally(() -> {
                        mRootView.hideLoading();
                    })
                    .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                    .map(file1 -> Luban.with(mApplication).load(file1).ignoreBy(400).get().get(0))
                    .observeOn(Schedulers.io())
                    .map(new Function<File, String>() {
                        @Override
                        public String apply(File file) throws Exception {
                            Random random = new Random();
                            int i = random.nextInt(100000);
                            long time = System.currentTimeMillis() / 1000;
                            LogUtils.e(file.length() / 1024 + "");
                            String imageToBase64 = imageToBase64(file);
                            Map<String, String> maps = new HashMap<>();
                            maps.put("app_id", TencentConstant.appid + "");
                            maps.put("image", imageToBase64);
                            maps.put("time_stamp", time + "");
                            maps.put("nonce_str", i + "ADERF");
                            maps.put("sign", "");
                            try {
                                String signature = TencentAISignSort.getSignature(maps);
                                LogUtils.e(signature);
                                maps.put("sign", signature);
                                Map<String, String> headers = new HashMap<>();
                                headers.put("Content-Type", "application/x-www-form-urlencoded");
                                String beautyValue = mModel.getBeautyValue(TencentConstant.url, headers, maps);
                                return beautyValue;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return "null";
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String beautyValue) throws Exception {
                            TencentAI tencentAI = new Gson().fromJson(beautyValue, TencentAI.class);
                            if (tencentAI.getRet() == 0 && "ok".equals(tencentAI.getMsg())) {
                                String image1 = tencentAI.getData().getImage();
                                byte[] decode = Base64.decode(image1, Base64.DEFAULT);
                                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/images/" + "IMG_" + new Date() + ".jpg";
                                FileIOUtils.writeFileFromBytesByStream(path, decode);
                                mRootView.loadImage(FileUtils.getFileByPath(path));
                            } else {
                                LogUtils.e(beautyValue);
                                mRootView.showMessage(tencentAI.getMsg());
                            }

                        }
                    });

        } else {
            LogUtils.e("file==null");
        }
    }

    private String imageToBase64(File image) {
        if (image.exists()) {
            byte[] bytes = FileIOUtils.readFile2BytesByStream(image);
            String result = Base64Util.encode(bytes);
            return result;
        } else {
            LogUtils.e("null");
            return "文件不存在";
        }
    }

    /**
     * 通过Uri返回File文件
     * 注意：通过相机的是类似content://media/external/images/media/97596
     * 通过相册选择的：file:///storage/sdcard0/DCIM/Camera/IMG_20150423_161955.jpg
     * 通过查询获取实际的地址
     *
     * @param uri
     * @return
     */
    private File getFileByUri(Uri uri) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = mApplication.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA}, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = mApplication.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        } else {
            LogUtils.i(TAG, "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void openCamera() {
        photofileName = "IMG_" + new Date() + ".jpg";
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/images");
        if (!file.exists()) {
            file.mkdirs();
        }
        File image = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/images", photofileName);

        imagUrl = FileProvider.getUriForFile(mApplication, mApplication.getResources().getString(R.string.authority), image);
        Intent intent = new Intent();
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imagUrl);//将拍取的照片保存到指定URI
        mRootView.launchActivityForResult(intent, 300);

    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mRootView.launchActivityForResult(intent, 200);
    }
}
