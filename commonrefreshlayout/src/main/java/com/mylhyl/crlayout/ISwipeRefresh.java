package com.mylhyl.crlayout;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

/**
 * Created by hupei on 2016/5/18.
 */
public interface ISwipeRefresh<T> {
    void autoRefresh();

    void autoRefresh(boolean scale, int start, int end);

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
