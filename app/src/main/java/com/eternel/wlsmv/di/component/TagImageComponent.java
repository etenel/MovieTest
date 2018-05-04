package com.eternel.wlsmv.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.eternel.wlsmv.di.module.TagImageModule;

import com.jess.arms.di.scope.FragmentScope;
import com.eternel.wlsmv.mvp.ui.fragment.TagImageFragment;

@FragmentScope
@Component(modules = TagImageModule.class, dependencies = AppComponent.class)
public interface TagImageComponent {
    void inject(TagImageFragment fragment);
}