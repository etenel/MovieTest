package com.eternel.wlsmv.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.eternel.wlsmv.mvp.contract.FutureMovieContract;
import com.eternel.wlsmv.mvp.model.FutureMovieModel;


@Module
public class FutureMovieModule {
    private FutureMovieContract.View view;

    /**
     * 构建FutureMovieModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public FutureMovieModule(FutureMovieContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    FutureMovieContract.View provideFutureMovieView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    FutureMovieContract.Model provideFutureMovieModel(FutureMovieModel model) {
        return model;
    }
}