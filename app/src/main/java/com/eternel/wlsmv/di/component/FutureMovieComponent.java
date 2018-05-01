package com.eternel.wlsmv.di.component;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.eternel.wlsmv.di.module.FutureMovieModule;

import com.eternel.wlsmv.mvp.ui.fragment.FutureMovieFragment;

@ActivityScope
@Component(modules = FutureMovieModule.class, dependencies = AppComponent.class)
public interface FutureMovieComponent {
    void inject(FutureMovieFragment fragment);
}