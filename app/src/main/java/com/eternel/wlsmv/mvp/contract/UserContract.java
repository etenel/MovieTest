/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eternel.wlsmv.mvp.contract;

import android.app.Activity;

import com.eternel.wlsmv.mvp.model.entity.ImageEntity;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;

/**
 * ================================================
 * 展示 Contract 的用法
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#2.4.1">Contract wiki 官方文档</a>
 * Created by JessYan on 09/04/2016 10:47
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface UserContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        void startLoadMore();
        void endLoadMore();
        Activity getActivity();
        //申请权限
        RxPermissions getRxPermissions();
    }
    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,如是否使用缓存
//    page：翻页值。从 1 开始。需要搭配 json 中的 pose_id 字段使用。可为空
//    post_id：如果是第一页则不需要添加该字段。否则，需要加上该字段，该字段的值为上一页最后一个 json 中的 post_id 值
//    type：如果是第一页则是 refresh，如果是加载更多，则是 loadmore。可为空
    interface Model extends IModel{
        Observable<ImageEntity> getFirstMovies(int page, boolean update, String type);
        Observable<ImageEntity> getMovies(int page, boolean update, String type, int post_id);
    }
}
