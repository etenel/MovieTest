package com.eternel.wlsmv.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.eternel.wlsmv.di.module.MovieDetailModule;

import com.jess.arms.di.scope.ActivityScope;
import com.eternel.wlsmv.mvp.ui.activity.MovieDetailActivity;

@ActivityScope
@Component(modules = MovieDetailModule.class, dependencies = AppComponent.class)
public interface MovieDetailComponent {
    void inject(MovieDetailActivity activity);
}