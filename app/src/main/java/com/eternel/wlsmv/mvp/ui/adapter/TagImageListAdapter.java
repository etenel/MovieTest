package com.eternel.wlsmv.mvp.ui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.eternel.wlsmv.R;
import com.eternel.wlsmv.imageloader.loader.ImageLoader;
import com.eternel.wlsmv.mvp.model.entity.TagDetailListEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by eternel
 * on 2018/5/4.
 */

public class TagImageListAdapter extends BaseQuickAdapter<TagDetailListEntity.PostListBean, BaseViewHolder> {
    private int types;

    public TagImageListAdapter(int layoutResId) {
        super(layoutResId);
        setMultiTypeDelegate(new MultiTypeDelegate<TagDetailListEntity.PostListBean>() {
            @Override
            protected int getItemType(TagDetailListEntity.PostListBean postListBean) {
                String type = postListBean.getType();
                switch (type) {
                    case "text":
                        types = 0;
                        break;
                    case "multi-photo":
                        types = 1;
                        break;
                }
                return TagImageListAdapter.this.types;
            }
        });
        getMultiTypeDelegate().registerItemType(0, R.layout.item_text)
                .registerItemType(1, R.layout.item_tag_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, TagDetailListEntity.PostListBean item) {
        switch (types) {
            case 0:
                helper.setText(R.id.tv_title, item.getTitle());
                JSONObject jsonObject = new JSONObject((Map) item.getTitle_image());
                try {
                    String url = jsonObject.getString("url");
                    ImageLoader.getInstance().loadImage(mContext, url, (ImageView) helper.getView(R.id.title_image));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                ImageLoader.getInstance().loadImage(mContext, item.getCover_image_src(),(ImageView)helper.getView(R.id.iv_image));

                break;
        }
    }
}
