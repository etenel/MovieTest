package com.eternel.wlsmv.mvp.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.eternel.wlsmv.R;
import com.eternel.wlsmv.di.component.DaggerFutureMovieComponent;
import com.eternel.wlsmv.di.module.FutureMovieModule;
import com.eternel.wlsmv.mvp.contract.FutureMovieContract;
import com.eternel.wlsmv.mvp.model.api.Api;
import com.eternel.wlsmv.mvp.presenter.FutureMoviePresenter;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.jess.arms.utils.ArmsUtils;

import java.io.File;
import java.net.URL;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import uk.co.senab.photoview.PhotoView;

import static android.app.Activity.RESULT_OK;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class FutureMovieFragment extends BaseFragment<FutureMoviePresenter> implements FutureMovieContract.View {

    @BindView(R.id.sd_view)
    PhotoView sdView;
    @BindView(R.id.bt_image)
    Button btImage;
    @BindView(R.id.bt_photo)
    Button btPhoto;
    @BindView(R.id.rl_progress)
    RelativeLayout rlProgress;

    public static FutureMovieFragment newInstance() {
        FutureMovieFragment fragment = new FutureMovieFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerFutureMovieComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .futureMovieModule(new FutureMovieModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_future_movie, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 通过此方法可以使 Fragment 能够与外界做一些交互和通信, 比如说外部的 Activity 想让自己持有的某个 Fragment 对象执行一些方法,
     * 建议在有多个需要与外界交互的方法时, 统一传 {@link Message}, 通过 what 字段来区分不同的方法, 在 {@link #setData(Object)}
     * 方法中就可以 {@code switch} 做不同的操作, 这样就可以用统一的入口方法做多个不同的操作, 可以起到分发的作用
     * <p>
     * 调用此方法时请注意调用时 Fragment 的生命周期, 如果调用 {@link #setData(Object)} 方法时 {@link Fragment#onCreate(Bundle)} 还没执行
     * 但在 {@link #setData(Object)} 里却调用了 Presenter 的方法, 是会报空的, 因为 Dagger 注入是在 {@link Fragment#onCreate(Bundle)} 方法中执行的
     * 然后才创建的 Presenter, 如果要做一些初始化操作,可以不必让外部调用 {@link #setData(Object)}, 在 {@link #initData(Bundle)} 中初始化就可以了
     * <p>
     * Example usage:
     * <pre>
     * public void setData(@Nullable Object data) {
     *     if (data != null && data instanceof Message) {
     *         switch (((Message) data).what) {
     *             case 0:
     *                 loadData(((Message) data).arg1);
     *                 break;
     *             case 1:
     *                 refreshUI();
     *                 break;
     *             default:
     *                 //do something
     *                 break;
     *         }
     *     }
     * }
     *
     * // call setData(Object):
     * Message data = new Message();
     * data.what = 0;
     * data.arg1 = 1;
     * fragment.setData(data);
     * </pre>
     *
     * @param data 当不需要参数时 {@code data} 可以为 {@code null}
     */
    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {
        rlProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        rlProgress.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
//        ArmsUtils.startActivity(intent);
        startActivityForResult(intent, 300);
    }


    @Override
    public void killMyself() {

    }


    @OnClick(R.id.bt_image)
    public void onBtImageClicked() {
       mPresenter.openGallery();
    }

    @OnClick(R.id.bt_photo)
    public void onBtPhotoClicked() {
        mPresenter.openCamera();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.e(data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200:
                    Uri image = data.getData();
                    mPresenter.addImage(image);
                    break;
                case 300:
                    Uri photo= Uri.parse("sss");
                    mPresenter.addImage(photo);
                    break;
            }
        }
    }

    @Override
    public void loadImage(File file) {
        LogUtils.e(file.getPath());
        GlideArms.with(sdView.getContext()).load(file).into(sdView);
    }

    @Override
    public void launchActivityForResult(Intent intent, int requestCode) {
        checkNotNull(intent);
        startActivityForResult(intent,requestCode);
    }
}
