package com.eternel.wlsmv.mvp.ui.adapter;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.eternel.wlsmv.R;
import com.eternel.wlsmv.imageloader.loader.ImageLoader;
import com.eternel.wlsmv.mvp.model.api.Api;
import com.eternel.wlsmv.mvp.model.entity.TagDetailListEntity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
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
//                try {
//                    JSONObject jsonObject = (JSONObject) item.getTitle_image();
//                    String url = jsonObject.getString("url");
//                    ImageLoader.getInstance().loadImage(mContext, url, (ImageView) helper.getView(R.id.title_image));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                break;
            case 1:
//                https://photo.tuchong.com/ + user_id +/l/ + img_id+.webp
                List<TagDetailListEntity.PostListBean.ImagesBean> images = item.getImages();
                if (images != null && images.size() > 0) {
                    TagDetailListEntity.PostListBean.ImagesBean imagesBean = images.get(0);
                    float width = imagesBean.getWidth();
                    float height = imagesBean.getHeight();
                    //  ImageLoader.getInstance().loadImage(mContext, item.getCover_image_src(), (ImageView) helper.getView(R.id.iv_image));
                    SimpleDraweeView view = helper.getView(R.id.iv_image);
                    view.setImageURI(Api.APP_PHOTO + imagesBean.getUser_id() + "/l/" + imagesBean.getImg_id() + ".webp");
                    float asr = width / height;
                    view.setAspectRatio(asr);
                    GenericDraweeHierarchyBuilder builder =
                            new GenericDraweeHierarchyBuilder(mContext.getResources());
                    GenericDraweeHierarchy hierarchy = builder
                            .setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP)
                            .build();
                    view.setHierarchy(hierarchy);
                    helper.setText(R.id.tv_count,images.size()+"");
                }

                break;
        }
    }
}
