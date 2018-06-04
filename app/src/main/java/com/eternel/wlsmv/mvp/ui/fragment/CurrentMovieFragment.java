package com.eternel.wlsmv.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eternel.wlsmv.R;
import com.eternel.wlsmv.di.component.DaggerCurrentMovieComponent;
import com.eternel.wlsmv.di.module.CurrentMovieModule;
import com.eternel.wlsmv.mvp.contract.CurrentMovieContract;
import com.eternel.wlsmv.mvp.presenter.CurrentMoviePresenter;
import com.eternel.wlsmv.mvp.ui.activity.MovieDetailActivity;
import com.eternel.wlsmv.mvp.ui.adapter.MovieListAdapter;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.qmuiteam.qmui.widget.QMUIAnimationListView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class CurrentMovieFragment extends BaseFragment<CurrentMoviePresenter> implements CurrentMovieContract.View {

    @BindView(R.id.rl_movies)
    RecyclerView rlMovies;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;

    @Inject
    MovieListAdapter movieListAdapter;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    public static CurrentMovieFragment newInstance() {
        CurrentMovieFragment fragment = new CurrentMovieFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerCurrentMovieComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .currentMovieModule(new CurrentMovieModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_movie, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        rlMovies.setAdapter(movieListAdapter);
        rlMovies.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPresenter.getMovies(true);
            }
        });
        refresh.setEnableLoadMore(false);
        mPresenter.getMovies(true);
        movieListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
               Intent intent = new Intent(getContext(), MovieDetailActivity.class);
               intent.putExtra("id",movieListAdapter.getItem(position).getId());
               startActivity(intent);
            }
        });
    }

    /**
     * 通过此方法可以使 Fragment 能够与外界做一些交互和通信, 比如说外部的 Activity 想让自己持有的某个 Fragment 对象执行一些方法,
     * 建议在有多个需要与外界交互的方法时, 统一传 {@link Message}, 通过 what 字段来区分不同的方法, 在 {@link #setData(Object)}
     * 方法中就可以 {@code switch} 做不同的操作, 这样就可以用统一的入口方法做多个不同的操作, 可以起到分发的作用
     * <p>
     * 调用此方法时请注意调用时 Fragment 的生命周期, 如果调用 {@link #setData(Object)} 方法时 {@link Fragment#onCreate(Bundle)} 还没执行
     * 但在 {@link #setData(Object)} 里却调用了 Presenter 的方法, 是会报空的, 因为 Dagger 注入是在 {@link Fragment#onCreate(Bundle)} 方法中执行的
     * 然后才创建的 Presenter, 如果要做一些初始化操作,可以不必让外部调用 {@link #setData(Object)}, 在 {@link #initData(Bundle)} 中初始化就可以了
     * <p>
     * Example usage:
     * <pre>
     * public void setData(@Nullable Object data) {
     *     if (data != null && data instanceof Message) {
     *         switch (((Message) data).what) {
     *             case 0:
     *                 loadData(((Message) data).arg1);
     *                 break;
     *             case 1:
     *                 refreshUI();
     *                 break;
     *             default:
     *                 //do something
     *                 break;
     *         }
     *     }
     * }
     *
     * // call setData(Object):
     * Message data = new Message();
     * data.what = 0;
     * data.arg1 = 1;
     * fragment.setData(data);
     * </pre>
     *
     * @param data 当不需要参数时 {@code data} 可以为 {@code null}
     */
    @Override
    public void setData(@Nullable Object data) {
        if (data != null && data instanceof Message) {
            switch (((Message) data).what) {
                case 0:
                    if (((Message) data).arg1 == 1) {
                        refresh.autoRefresh();
                    }
                    break;
            }
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        if (refresh != null) {
            refresh.finishRefresh();
        }
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

    public void setTitle(String title) {
        checkNotNull(title);
        tvTitle.setText(title);
    }

    @Override
    public void hideLoadMore() {
        if (refresh != null) {
            refresh.finishLoadMore();
        }
    }


}
