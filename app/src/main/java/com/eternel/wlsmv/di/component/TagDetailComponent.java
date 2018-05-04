package com.eternel.wlsmv.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.eternel.wlsmv.di.module.TagDetailModule;

import com.jess.arms.di.scope.ActivityScope;
import com.eternel.wlsmv.mvp.ui.activity.TagDetailActivity;

@ActivityScope
@Component(modules = TagDetailModule.class, dependencies = AppComponent.class)
public interface TagDetailComponent {
    void inject(TagDetailActivity activity);
}