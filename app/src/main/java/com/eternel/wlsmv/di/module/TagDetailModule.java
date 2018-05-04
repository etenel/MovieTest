package com.eternel.wlsmv.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.eternel.wlsmv.mvp.contract.TagDetailContract;
import com.eternel.wlsmv.mvp.model.TagDetailModel;


@Module
public class TagDetailModule {
    private TagDetailContract.View view;

    /**
     * 构建TagDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public TagDetailModule(TagDetailContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    TagDetailContract.View provideTagDetailView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    TagDetailContract.Model provideTagDetailModel(TagDetailModel model) {
        return model;
    }
}