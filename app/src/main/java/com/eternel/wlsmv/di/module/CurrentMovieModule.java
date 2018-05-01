package com.eternel.wlsmv.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.eternel.wlsmv.mvp.contract.CurrentMovieContract;
import com.eternel.wlsmv.mvp.model.CurrentMovieModel;


@Module
public class CurrentMovieModule {
    private CurrentMovieContract.View view;

    /**
     * 构建CurrentMovieModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CurrentMovieModule(CurrentMovieContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CurrentMovieContract.View provideCurrentMovieView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CurrentMovieContract.Model provideCurrentMovieModel(CurrentMovieModel model) {
        return model;
    }
}