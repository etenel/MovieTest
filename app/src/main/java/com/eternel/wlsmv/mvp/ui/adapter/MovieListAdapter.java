package com.eternel.wlsmv.mvp.ui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eternel.wlsmv.R;
import com.eternel.wlsmv.mvp.model.entity.MoviesEntity;
import com.jess.arms.http.imageloader.glide.GlideArms;

import java.util.List;

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

        GlideArms.with(mContext).load(item.getImages().getLarge()).into((ImageView) helper.getView(R.id.iv_cover));
        helper.setText(R.id.tv_movie_name, item.getTitle());
        MoviesEntity.SubjectsBean.RatingBean rating = item.getRating();
        String star;
        if (rating != null) {
            double average = rating.getAverage();
            if(average!=0) {
                star = "评分" + average;
            }else{
                star = "评价人数不足";
            }
        } else {
            star = "评价人数不足";
        }
        helper.setText(R.id.tv_star, star);

        List<MoviesEntity.SubjectsBean.DirectorsBean> directors = item.getDirectors();
        StringBuilder stringDirectors = new StringBuilder();
        stringDirectors.append("导演：");
        if (directors != null && directors.size() > 0) {
            for (int i = 0; i < directors.size(); i++) {
                if(i==0) {
                    stringDirectors.append(directors.get(i).getName());
                }else{
                    stringDirectors.append("，").append(directors.get(i).getName());

                }
            }
        }
        helper.setText(R.id.tv_directors, stringDirectors.toString().trim());

        List<MoviesEntity.SubjectsBean.CastsBean> casts = item.getCasts();
        StringBuilder stringCasts=new StringBuilder();
        stringCasts.append("演员：");
        if(casts!=null&&casts.size()>0) {
            for(int i = 0; i < casts.size(); i++) {
                if(i==0) {
                    stringCasts.append(casts.get(i).getName());
                }else{
                    stringCasts.append("，").append(casts.get(i).getName());
                }
            }
        }
        helper.setText(R.id.tv_casts,stringCasts.toString().trim());

    }
}
