package com.eternel.wlsmv.imageloader.glide;

import com.bumptech.glide.load.engine.GlideException;

/**
 * Created by eternel
 * on 2018/4/6.
 */

public interface OnGlideImageViewListener {
    void onProgress(int percent, boolean isDone, GlideException exception);
}
