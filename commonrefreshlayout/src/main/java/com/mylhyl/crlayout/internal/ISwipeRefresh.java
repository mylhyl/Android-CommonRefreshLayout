package com.mylhyl.crlayout.internal;

import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

/**
 * Created by hupei on 2016/5/18.
 */
public interface ISwipeRefresh<T> {
    /**
     * 显示刷新动画，颜色随系统
     */
    void autoRefresh();

    /**
     * 显示刷新动画，可指定颜色
     *
     * @param colorResIds 动画颜色
     */
    void autoRefresh(@ColorRes int... colorResIds);

    void autoRefresh(boolean scale, int start, int end, @ColorRes int... colorResIds);

    void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener);

    void setEmptyText(CharSequence text);

    void setEmptyText(int resId);

    void setEmptyView(View emptyView);

    View getEmptyView();

    void setRefreshing(boolean refreshing);

    /**
     * 获取可滑动的 ListView、ExpandableListView、GridView、RecyclerView
     *
     * @return
     */
    T getScrollView();


    SwipeRefreshLayout getSwipeRefreshLayout();
}
