package com.eternel.wlsmv.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.eternel.wlsmv.di.module.PictureModule;

import com.eternel.wlsmv.mvp.ui.activity.PictureActivity;

@ActivityScope
@Component(modules = PictureModule.class, dependencies = AppComponent.class)
public interface PictureComponent {
    void inject(PictureActivity activity);
}