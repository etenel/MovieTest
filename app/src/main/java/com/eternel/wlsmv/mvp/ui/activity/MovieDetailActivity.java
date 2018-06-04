package com.eternel.wlsmv.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.eternel.wlsmv.R;
import com.eternel.wlsmv.di.component.DaggerMovieDetailComponent;
import com.eternel.wlsmv.di.module.MovieDetailModule;
import com.eternel.wlsmv.mvp.contract.MovieDetailContract;
import com.eternel.wlsmv.mvp.model.entity.MovieDetailEntity;
import com.eternel.wlsmv.mvp.presenter.MovieDetailPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MovieDetailActivity extends BaseActivity<MovieDetailPresenter> implements MovieDetailContract.View {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    private String id;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMovieDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .movieDetailModule(new MovieDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_movie_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        id = getIntent().getStringExtra("id");
        mPresenter.getMovieDetail(id);
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

    @Override
    public void setDetail(MovieDetailEntity movieDetailEntity) {
        tvTitle.setText(movieDetailEntity.getTitle());
    }

}
