package com.eternel.wlsmv.mvp.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.eternel.wlsmv.R;
import com.eternel.wlsmv.di.component.DaggerPictureComponent;
import com.eternel.wlsmv.di.module.PictureModule;
import com.eternel.wlsmv.mvp.contract.PictureContract;
import com.eternel.wlsmv.mvp.model.api.Api;
import com.eternel.wlsmv.mvp.model.entity.TagDetailListEntity;
import com.eternel.wlsmv.mvp.presenter.PicturePresenter;
import com.eternel.wlsmv.mvp.ui.adapter.PictureAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.jess.arms.utils.ArmsUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class PictureActivity extends BaseActivity<PicturePresenter> implements PictureContract.View {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.rl_content)
    RelativeLayout rlContent;
    private PictureAdapter pictureAdapter;
    private List<String> imageurl;
    private View snackBarRootView;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPictureComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .pictureModule(new PictureModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_picture; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toFullScreen();
        TagDetailListEntity.PostListBean image = (TagDetailListEntity.PostListBean) getIntent().getSerializableExtra("image");
        List<TagDetailListEntity.PostListBean.ImagesBean> images = image.getImages();
        imageurl = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            TagDetailListEntity.PostListBean.ImagesBean imagesBean = images.get(i);
            imageurl.add(Api.APP_PHOTO + imagesBean.getUser_id() + "/f/" + imagesBean.getImg_id() + ".jpg");
        }
        pictureAdapter = new PictureAdapter(imageurl);
        viewpager.setAdapter(pictureAdapter);
        viewpager.setCurrentItem(0);
        initListener();
    }

    private void initListener() {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateNumberText(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pictureAdapter.setOnImageClickListener(new PictureAdapter.onImageClickListener() {
            @Override
            public void onImageClick(View view, int position) {
                if (view instanceof PhotoView) {
                    float scale = ((PhotoView) view).getScale();
                    LogUtils.e(scale + "");
                    if (scale != 1) {
                        ((PhotoView) view).setScale(1);
                    } else {
                        killMyself();
                    }
                }
            }

            @Override
            public void onImageLongClick(View view, int position) {
                showSavePictureDialog(imageurl.get(position));
            }
        });
    }

    private void showSavePictureDialog(final String imageUrl) {
        QMUIDialog.MenuDialogBuilder builder = new QMUIDialog.MenuDialogBuilder(this);
        builder.addItem("保存图片", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GlideArms.with(PictureActivity.this).downloadOnly().load(Uri.parse(imageUrl)).into(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                        File filePath = new File(Environment.getExternalStorageDirectory() + "/image/");
                        if (!filePath.exists()) {
                            if (!filePath.mkdirs()) {
                                showMessage("创建文件夹失败了");
                                return;
                            }
                        }
                        File file = new File(filePath, UUID.randomUUID().toString() + ".jpg");
                        FileUtils.copyFile(resource, file);
                        showMessage("保存图片成功了");
                        notifySystemGallery(file);
                    }
                });
                dialog.dismiss();
                toFullScreen();
            }
        });
        builder.show();
    }

    private void toFullScreen() {
        View decorView = getWindow().getDecorView();
        if (decorView != null) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    private void updateNumberText(int position) {
        if (imageurl == null) {
            return;
        }
        if (position < 0) {
            position = 0;
        }
        tvNum.setText((position + 1) + "/" + imageurl.size());
    }

    private void notifySystemGallery(File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(getApplicationContext(), getString(R.string.authority), file);
        } else {
            uri = Uri.fromFile(file);
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(uri);
        sendBroadcast(intent);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
//        snackBarRootView = findViewById(android.R.id.content);
//        SnackbarUtils.with(snackBarRootView)
//                .setBottomMargin(SizeUtils.dp2px(40))
//                .setMessage(message)
//                .setMessageColor(getResources().getColor(R.color.aliceblue))
//                .setBgColor(getResources().getColor(R.color.backcolor))
//                .setDuration(SnackbarUtils.LENGTH_SHORT)
//                .show();
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


    @OnClick(R.id.rl_content)
    public void onViewClicked() {
        killMyself();
    }
}
