package com.eternel.wlsmv.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.LogUtils;
import com.eternel.wlsmv.R;
import com.eternel.wlsmv.di.component.DaggerTagDetailComponent;
import com.eternel.wlsmv.di.module.TagDetailModule;
import com.eternel.wlsmv.mvp.contract.TagDetailContract;
import com.eternel.wlsmv.mvp.presenter.TagDetailPresenter;
import com.eternel.wlsmv.mvp.ui.adapter.TabAdapter;
import com.eternel.wlsmv.mvp.ui.fragment.TagImageFragment;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class TagDetailActivity extends BaseActivity<TagDetailPresenter> implements TagDetailContract.View {

    @BindView(R.id.m_tab)
    TabLayout mTab;
    @BindView(R.id.m_pager)
    ViewPager mPager;
    private int tab = 0;
    private List<Fragment> mFragments;
    private List<String> titles;
    private TagImageFragment HotFragment;
    private TagImageFragment NewFragment;
    private String tag_name;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTagDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .tagDetailModule(new TagDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_tag_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tag_name = getIntent().getStringExtra("tag_name");
        LogUtils.e(tag_name);
        if (savedInstanceState == null) {
            HotFragment = TagImageFragment.newInstance(tag_name,"weekly");
            NewFragment = TagImageFragment.newInstance(tag_name,"new");
        } else {
            tab = savedInstanceState.getInt("fragment");
            FragmentManager fm = getSupportFragmentManager();
            HotFragment = (TagImageFragment) FragmentUtils.findFragment(fm, TagImageFragment.class);
            NewFragment = (TagImageFragment) FragmentUtils.findFragment(fm, TagImageFragment.class);
        }
        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(HotFragment);
            mFragments.add(NewFragment);
        }
        if (titles == null) {
            titles = new ArrayList<>();
            titles.add("热门");
            titles.add("最新");
        }
        mPager.setAdapter(new TabAdapter(getSupportFragmentManager(), mFragments, titles));
        mTab.setupWithViewPager(mPager);
        mPager.setCurrentItem(0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("fragment", tab);
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
