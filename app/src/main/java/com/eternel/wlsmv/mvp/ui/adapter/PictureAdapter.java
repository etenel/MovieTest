package com.eternel.wlsmv.mvp.ui.adapter;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.eternel.wlsmv.R;
import com.jess.arms.http.imageloader.glide.GlideArms;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @author flymegoc
 * @date 2018/1/25
 */

public class PictureAdapter extends PagerAdapter {

    private List<String> imageList;
    private onImageClickListener onImageClickListener;

    public PictureAdapter(List<String> imageList) {
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList == null ? 0 : imageList.size();
    }

    @NonNull
    @Override
    public View instantiateItem(@NonNull ViewGroup container, final int position) {
        View contentView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_picture_adapter, container, false);
        PhotoView photoView = contentView.findViewById(R.id.photoView);
        final ProgressBar progressBar = contentView.findViewById(R.id.progressBar);
        String url = imageList.get(position);
        GlideArms.with(container).load(buildGlideUrl(url)).transition(new DrawableTransitionOptions().crossFade(300)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(photoView);
        // Now just add PhotoView to ViewPager and return it
        container.addView(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onImageClickListener != null) {
                    onImageClickListener.onImageLongClick(v, position);
                }
                return true;
            }
        });
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (onImageClickListener != null) {
                    onImageClickListener.onImageClick(view, position);
                }
            }
        });
        return contentView;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public interface onImageClickListener {
        void onImageClick(View view, int position);

        void onImageLongClick(View view, int position);
    }

    public void setOnImageClickListener(PictureAdapter.onImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    private GlideUrl buildGlideUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        } else {
            return new GlideUrl(url, new LazyHeaders.Builder()
                    .build());
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
     //   super.destroyItem(container, position, object);
        FrameLayout view = (FrameLayout) object;
        for (int i = 0; i < view.getChildCount(); i++) {
            View childView = view.getChildAt(i);
            if (childView instanceof PhotoView) {
                childView.setOnClickListener(null);
                childView.setOnLongClickListener(null);
                GlideArms.with(container).clear(childView);
                view.removeViewAt(i);
            }
        }
        container.removeView(view);
    }
}
