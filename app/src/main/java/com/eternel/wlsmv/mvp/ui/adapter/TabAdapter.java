package com.eternel.wlsmv.mvp.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by eternel
 * on 2018/4/30.
 */

public class TabAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments;
    private final List<String> titles;

    public TabAdapter(FragmentManager fm, List<Fragment> mFragments, List<String> titles) {
        super(fm);
        this.mFragments=mFragments;
        this.titles=titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
