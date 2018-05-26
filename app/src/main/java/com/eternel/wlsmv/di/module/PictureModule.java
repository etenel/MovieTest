package com.eternel.wlsmv.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.eternel.wlsmv.mvp.contract.PictureContract;
import com.eternel.wlsmv.mvp.model.PictureModel;


@Module
public class PictureModule {
    private PictureContract.View view;

    /**
     * 构建PictureModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public PictureModule(PictureContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    PictureContract.View providePictureView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    PictureContract.Model providePictureModel(PictureModel model) {
        return model;
    }
}