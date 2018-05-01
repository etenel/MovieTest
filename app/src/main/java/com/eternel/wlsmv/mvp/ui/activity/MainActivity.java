package com.eternel.wlsmv.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.blankj.utilcode.util.FragmentUtils;
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
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.m_pager)
    ViewPager mPager;
    @BindView(R.id.m_tab)
    TabLayout mTab;
    private ImageFragment imageFragment;
    private CurrentMovieFragment currentMovieFragment;
    private FutureMovieFragment futureMovieFragment;
    private int tab = 0;
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
            tab = savedInstanceState.getInt("fragment");
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
        mPager.setAdapter(new TabAdapter(getSupportFragmentManager(), mFragments, titles));
        mPager.setOffscreenPageLimit(3);
        mTab.setupWithViewPager(mPager);
        mTab.getTabAt(0).setIcon(R.drawable.image_tab);
        mTab.getTabAt(1).setIcon(R.drawable.hot_movie);
        mTab.getTabAt(2).setIcon(R.drawable.future_movie);
        mPager.setCurrentItem(0);
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
        outState.putInt("fragment", tab);
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

}
