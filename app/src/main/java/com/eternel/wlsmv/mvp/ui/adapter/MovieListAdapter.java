package com.eternel.wlsmv.mvp.ui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eternel.wlsmv.R;
import com.eternel.wlsmv.mvp.model.entity.MoviesEntity;
import com.jess.arms.http.imageloader.glide.GlideArms;

/**
 * Created by etenel
 * on 2018/5/28.
 */

public class MovieListAdapter extends BaseQuickAdapter<MoviesEntity.SubjectsBean, BaseViewHolder> {
    public MovieListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MoviesEntity.SubjectsBean item) {

        GlideArms.with(mContext).load(item.getImages().getLarge()).into((ImageView)helper.getView(R.id.iv_cover));
    }
}
