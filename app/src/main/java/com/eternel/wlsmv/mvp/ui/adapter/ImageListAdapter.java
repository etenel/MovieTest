package com.eternel.wlsmv.mvp.ui.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.load.engine.GlideException;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.eternel.wlsmv.R;
import com.eternel.wlsmv.imageloader.glide.CircleProgressView;
import com.eternel.wlsmv.imageloader.glide.processlistener.OnProgressListener;
import com.eternel.wlsmv.imageloader.loader.ImageLoader;
import com.eternel.wlsmv.mvp.model.entity.ImageEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by eternel
 * on 2018/4/29.
 */

public class ImageListAdapter extends BaseQuickAdapter<ImageEntity.FeedListBean, BaseViewHolder> {

    private int type;

    public ImageListAdapter(int layoutResId) {
        super(layoutResId);
        setMultiTypeDelegate(new MultiTypeDelegate<ImageEntity.FeedListBean>() {
            @Override
            protected int getItemType(ImageEntity.FeedListBean feedListBean) {
                switch (feedListBean.getType()) {
                    case "text":
                        type = 0;
                        break;
                    case "multi-photo":
                        type = 1;
                        break;
                    default:
                        type = 1;
                }
                return type;
            }
        });
        getMultiTypeDelegate().registerItemType(0, R.layout.item_text)
                .registerItemType(1, R.layout.item_images);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageEntity.FeedListBean item) {
        switch (type) {
            case 0:

                try {
                    JSONObject jsonObject = new JSONObject((Map) item.getTitle_image());
                    ImageLoader.getInstance().loadImage(mContext, jsonObject.getString("url"),
                            (ImageView) helper.getView(R.id.title_image));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                helper.setText(R.id.tv_title, item.getTitle());
                break;

            case 1:
                helper.setText(R.id.tv_name, item.getPassed_time())
                        .setText(R.id.tv_tags, item.getTitle())
                        .addOnClickListener(R.id.line_image);
                LinearLayout view = helper.getView(R.id.line_image);
                if (item.getImages().size() > 0) {
                    view.setVisibility(View.VISIBLE);
                    for (int i = 0; i < item.getImages().size(); i++) {
                        ImageEntity.FeedListBean.ImagesBean imagesBean = item.getImages().get(i);
                        ImageView imageView = new ImageView(mContext);
                        //        https://photo.tuchong.com/ + user_id +/f/ + img_id+.jpg
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        if (imagesBean.getHeight() <= view.getHeight()) {
                            params.height = imagesBean.getHeight();
                        } else {
                            params.height = view.getHeight();
                        }
                        if (imagesBean.getWidth() <= view.getWidth()) {
                            params.width = imagesBean.getWidth();
                        }
                        imageView.setLayoutParams(params);
                        view.addView(imageView);
                        ImageLoader.getInstance().loadImage(mContext, "https://photo.tuchong.com/" + imagesBean.getUser_id() +
                                "/f/" + imagesBean.getImg_id() + ".jpg", imageView);
                    }
                } else {
                    view.setVisibility(View.GONE);
                }

                break;
        }

    }
}
