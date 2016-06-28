package com.mylhyl.crlayout.app;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;

import com.mylhyl.crlayout.SwipeRefreshAbsListView;


abstract class SwipeRefreshAbsListFragment<T extends SwipeRefreshAbsListView> extends SwipeRefreshAdapterFragment<T> {
    private final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /**
             * ListView or GridView 单点事件
             */
            onListItemClick(parent, view, position, id);
        }
    };


    /**
     * Item 点击时此方法调用，子类可重写
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    public void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    /**
     * 适配器设置数据源
     *
     * @param adapter
     * @author hupei
     * @date 2015年7月31日 上午9:06:14
     */
    public final void setListAdapter(ListAdapter adapter) {
        AbsListView absListView = (AbsListView) getSwipeRefreshLayout().getScrollView();
        if (absListView instanceof ExpandableListView) {
            new RuntimeException("please call SwipeRefreshExpandableListFragment.setListAdapter()");
        }
        T swipeRefreshLayout = getSwipeRefreshLayout();
        if (swipeRefreshLayout != null && swipeRefreshLayout instanceof SwipeRefreshAbsListView) {
            absListView.setVisibility(View.VISIBLE);
            absListView.setOnItemClickListener(mOnItemClickListener);

            swipeRefreshLayout.setAdapter(adapter);
        }
    }
}
