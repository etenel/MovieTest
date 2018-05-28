package com.eternel.wlsmv.mvp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.eternel.wlsmv.R;
import com.eternel.wlsmv.di.component.DaggerMainComponent;
import com.eternel.wlsmv.di.module.MainModule;
import com.eternel.wlsmv.mvp.contract.MainContract;
import com.eternel.wlsmv.mvp.presenter.MainPresenter;
import com.eternel.wlsmv.mvp.ui.adapter.TabAdapter;
import com.eternel.wlsmv.mvp.ui.fragment.CurrentMovieFragment;
import com.eternel.wlsmv.mvp.ui.fragment.FutureMovieFragment;
import com.eternel.wlsmv.mvp.ui.fragment.ImageFragment;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.m_pager)
    ViewPager mPager;
    @BindView(R.id.m_tab)
    TabLayout mTab;
    @BindView(R.id.fl_imagetag)
    FloatingActionButton flImagetag;
    private ImageFragment imageFragment;
    private CurrentMovieFragment currentMovieFragment;
    private FutureMovieFragment futureMovieFragment;
    private int selectTab = 0;
    private List<Fragment> mFragments;
    private List<String> titles;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            imageFragment = ImageFragment.newInstance();
            currentMovieFragment = CurrentMovieFragment.newInstance();
            futureMovieFragment = FutureMovieFragment.newInstance();
        } else {
            selectTab = savedInstanceState.getInt("fragment");
            FragmentManager fm = getSupportFragmentManager();
            imageFragment = (ImageFragment) FragmentUtils.findFragment(fm, ImageFragment.class);
            currentMovieFragment = (CurrentMovieFragment) FragmentUtils.findFragment(fm, CurrentMovieFragment.class);
            futureMovieFragment = (FutureMovieFragment) FragmentUtils.findFragment(fm, FutureMovieFragment.class);
        }
        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(imageFragment);
            mFragments.add(currentMovieFragment);
            mFragments.add(futureMovieFragment);
        }
        if (titles == null) {
            titles = new ArrayList<>();
            titles.add("图片");
            titles.add("热映");
            titles.add("预告");
        }
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            initfragment();
                        } else {
                            showMessage("获取权限失败");
                            initfragment();
                        }
                    }
                });

    }

    private void initfragment() {
        mPager.setAdapter(new TabAdapter(getSupportFragmentManager(), mFragments, titles));
        mPager.setOffscreenPageLimit(3);
        mTab.setupWithViewPager(mPager);
        mTab.getTabAt(0).setIcon(R.drawable.image_tab);
        mTab.getTabAt(1).setIcon(R.drawable.hot_movie);
        mTab.getTabAt(2).setIcon(R.drawable.future_movie);
        mPager.setCurrentItem(selectTab);
        mTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTab = tab.getPosition();
                switch (selectTab) {
                    case 0:
                        flImagetag.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        flImagetag.setVisibility(View.GONE);

                        break;
                    case 2:
                        flImagetag.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Message data = new Message();

                switch (tab.getPosition()) {
                    case 0:
                        data.what = 0;
                        data.arg1 = 1;
                        imageFragment.setData(data);
                        break;
                    case 1:
                        data.what = 0;
                        data.arg1 = 1;
                        currentMovieFragment.setData(data);
                        break;
                    case 2:

                        break;
                }
                if (tab.getPosition() == 0) {

                }
            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("fragment", selectTab);
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
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


    @OnClick(R.id.fl_imagetag)
    public void onViewClicked() {
        switch (selectTab) {
            case 0:
                new QMUIBottomSheet.BottomListSheetBuilder(this, true)
                        .addItem(R.drawable.image_tab, "题材", "subject")
                        .addItem(R.drawable.image_tab, "风格", "style")
                        .addItem(R.drawable.image_tab, "器材", "equipment")
                        .addItem(R.drawable.image_tab, "地区", "location")
                        .setCheckedIndex(SPUtils.getInstance(getResources().getString(R.string.tags)).getInt(getResources().getString(R.string.order), 0))
                        .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                dialog.dismiss();
                                SPUtils.getInstance(getResources().getString(R.string.tags)).put(getResources().getString(R.string.tags), tag);
                                SPUtils.getInstance(getResources().getString(R.string.tags)).put(getResources().getString(R.string.order), position);
                                if (imageFragment == null) {
                                    imageFragment = ImageFragment.newInstance();
                                }
                                Message data = new Message();
                                data.what = 0;
                                data.arg1 = 1;
                                imageFragment.setData(data);
                            }
                        })
                        .build()
                        .show();
                break;

        }
    }
}
