package com.eternel.wlsmv.di.module;

import com.eternel.wlsmv.R;
import com.eternel.wlsmv.mvp.model.entity.ImageEntity;
import com.eternel.wlsmv.mvp.ui.adapter.ImageListAdapter;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.eternel.wlsmv.mvp.contract.ImageContract;
import com.eternel.wlsmv.mvp.model.ImageModel;

import java.util.ArrayList;
import java.util.List;


@Module
public class ImageModule {
    private ImageContract.View view;

    /**
     * 构建ImageModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ImageModule(ImageContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ImageContract.View provideImageView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ImageContract.Model provideImageModel(ImageModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    List<ImageEntity.FeedListBean> provideImages() {
        return new ArrayList<>();
    }
    @ActivityScope
    @Provides
    ImageListAdapter provideMovieAdapter() {
        return new ImageListAdapter(R.layout.item_images);
    }

}