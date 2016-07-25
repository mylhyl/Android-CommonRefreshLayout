package com.mylhyl.crlayout.app;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mylhyl.crlayout.BaseSwipeRefresh;
import com.mylhyl.crlayout.internal.ISwipeRefresh;

/**
 * Google自家下拉刷新 SwipeRefreshLayout <br>
 * <p/>
 * 注：SwipeRefreshLayout 只能有一个 childView，childView自身必须是可滚动的view<br>
 * 或 childView 必须包含可滚动的view，如ScrollView或者AbsListView<br>
 * 子类继承重写 onCreateView 必须 super
 */
abstract class BaseSwipeRefreshFragment<T extends BaseSwipeRefresh> extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener {

    /**
     * 创建下拉刷新控件子类重写
     *
     * @return
     */
    public abstract T createSwipeRefreshLayout();

    protected T mSwipeRefresh;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mSwipeRefresh = createSwipeRefreshLayout();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 注册下拉刷新
        mSwipeRefresh.setOnRefreshListener(this);
    }

    public final void autoRefresh() {
        getSwipeRefreshLayout().autoRefresh();
    }

    public final void autoRefresh(@ColorRes int... colorResIds) {
        getSwipeRefreshLayout().autoRefresh(colorResIds);
    }

    /**
     * {@link ISwipeRefresh#setRefreshing(boolean)}
     */
    public final void setRefreshing(boolean refreshing) {
        getSwipeRefreshLayout().setRefreshing(refreshing);
    }


    public final void setEmptyText(int resId) {
        setEmptyText(getString(resId));
    }

    public final void setEmptyText(CharSequence text) {
        getSwipeRefreshLayout().setEmptyText(text);
    }

    public final void setEmptyView(View emptyView) {
        getSwipeRefreshLayout().setEmptyView(emptyView);
    }

    public final T getSwipeRefreshLayout() {
        return mSwipeRefresh;
    }
}
