package com.eternel.wlsmv.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.eternel.wlsmv.di.module.CurrentMovieModule;

import com.eternel.wlsmv.mvp.ui.fragment.CurrentMovieFragment;

@ActivityScope
@Component(modules = CurrentMovieModule.class, dependencies = AppComponent.class)
public interface CurrentMovieComponent {
    void inject(CurrentMovieFragment fragment);
}