package com.eternel.wlsmv.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.eternel.wlsmv.di.module.ImageModule;

import com.eternel.wlsmv.mvp.ui.fragment.ImageFragment;

@ActivityScope
@Component(modules = ImageModule.class, dependencies = AppComponent.class)
public interface ImageComponent {
    void inject(ImageFragment fragment);
}