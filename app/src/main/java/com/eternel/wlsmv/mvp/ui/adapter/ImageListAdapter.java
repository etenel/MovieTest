package com.eternel.wlsmv.mvp.ui.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.eternel.wlsmv.R;
import com.eternel.wlsmv.imageloader.loader.ImageLoader;
import com.eternel.wlsmv.mvp.model.api.Api;
import com.eternel.wlsmv.mvp.model.entity.ImageEntity;
import com.eternel.wlsmv.mvp.model.entity.ImageTagsEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by eternel
 * on 2018/4/29.
 */

public class ImageListAdapter extends BaseQuickAdapter<ImageTagsEntity.DataBean.TagListBean, BaseViewHolder> {

    private int type;

    public ImageListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageTagsEntity.DataBean.TagListBean item) {
        //未压缩图片
        //        https://photo.tuchong.com/ + user_id +/f/ + img_id+.jpg
        //封面压缩图
        //        https://photo.tuchong.com/ + user_id +/l/ + img_id+.webp
        helper.setText(R.id.tv_tags, item.getTag_name());
        List<ImageTagsEntity.DataBean.TagListBean.PostListBean> postList = item.getPostList();
        if (postList != null && postList.size() > 0) {
            ImageTagsEntity.DataBean.TagListBean.PostListBean.CoverImageBean.SourceBean source;
            switch (postList.size()) {
                case 2:
                    source = postList.get(1).getCover_image().getSource();
                    ImageLoader.getInstance().loadImage(mContext, source.getG(), (ImageView) helper.getView(R.id.iv_image));
                    break;
                case 3:
                    source = postList.get(1).getCover_image().getSource();
                    ImageLoader.getInstance().loadImage(mContext, source.getG(), (ImageView) helper.getView(R.id.iv_image));
                    break;
                default:
                    source = postList.get(0).getCover_image().getSource();
                    ImageLoader.getInstance().loadImage(mContext, source.getG(), (ImageView) helper.getView(R.id.iv_image));
                    break;
            }
        }

    }
}
