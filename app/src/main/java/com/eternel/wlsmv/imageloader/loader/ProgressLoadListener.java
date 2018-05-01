package com.eternel.wlsmv.imageloader.loader;

/**
 * Created by eternel
 * on 2018/4/6.
 */

public interface ProgressLoadListener {

    void update(int bytesRead, int contentLength);

    void onException();

    void onResourceReady();
}
