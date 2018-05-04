package com.eternel.wlsmv.di.module;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

import com.eternel.wlsmv.mvp.contract.TagImageContract;
import com.eternel.wlsmv.mvp.model.TagImageModel;


@Module
public class TagImageModule {
    private TagImageContract.View view;

    /**
     * 构建TagImageModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public TagImageModule(TagImageContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    TagImageContract.View provideTagImageView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    TagImageContract.Model provideTagImageModel(TagImageModel model) {
        return model;
    }
}