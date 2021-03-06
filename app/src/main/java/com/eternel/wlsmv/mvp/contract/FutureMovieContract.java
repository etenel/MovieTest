package com.eternel.wlsmv.mvp.contract;

import android.content.Intent;

import com.eternel.wlsmv.mvp.model.entity.ImageEntity;
import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

import java.io.File;
import java.util.Map;

import io.reactivex.Observable;


public interface FutureMovieContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
void loadImage(File file);
void launchActivityForResult(Intent intent,int requestCode );
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        String getBeautyValue(String url, Map<String,String>heads,Map<String,String>content);
    }
}
