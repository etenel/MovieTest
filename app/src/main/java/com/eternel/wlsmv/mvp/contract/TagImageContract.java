package com.eternel.wlsmv.mvp.contract;

import com.eternel.wlsmv.mvp.model.entity.TagDetailListEntity;
import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;

import io.reactivex.Observable;


public interface TagImageContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void endLoadMore();

        String getTagName();

        String getOrder();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        Observable<TagDetailListEntity> getTagDetailList(String tag, int page, int count, String order);
    }
}
