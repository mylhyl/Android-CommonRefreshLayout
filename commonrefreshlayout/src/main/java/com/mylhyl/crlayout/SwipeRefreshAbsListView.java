package com.mylhyl.crlayout;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.mylhyl.crlayout.internal.ILoadSwipeRefresh;

/**
 * SwipeRefreshLayout 加 AbsListView 布局<br>
 * 子类必须实现 {@linkplain BaseSwipeRefresh#createScrollView(Context, AttributeSet)}  createScrollView}方法
 * <p> Created by hupei on 2016/5/12.
 */
public abstract class SwipeRefreshAbsListView<T extends AbsListView> extends SwipeRefreshAdapterView<T> {
    private ListAdapter mEmptyDataSetAdapter;
    private EmptyDataSetObserver mDataSetObserver;

    public SwipeRefreshAbsListView(Context context) {
        super(context);
    }

    public SwipeRefreshAbsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public final void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        getScrollView().setOnItemClickListener(listener);
    }

    /**
     * 为 AbsListView 设置适配器
     *
     * @param adapter
     */
    public final void setAdapter(ListAdapter adapter) {
        if (adapter == null)
            throw new NullPointerException("mAdapter is null please call SwipeRefreshAbsListView.setAdapter");
        getScrollView().setOnScrollListener(new OnScrollAbsListListener(this));
        getScrollView().setAdapter(adapter);
        registerDataSetObserver(adapter);
    }

    final void registerDataSetObserver(ListAdapter adapter) {
        mEmptyDataSetAdapter = adapter;
        if (mEmptyDataSetAdapter != null && mDataSetObserver != null) {
            mEmptyDataSetAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        if (mEmptyDataSetAdapter != null && mDataSetObserver == null) {
            mDataSetObserver = new EmptyDataSetObserver();
            mEmptyDataSetAdapter.registerDataSetObserver(mDataSetObserver);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mEmptyDataSetAdapter != null && mDataSetObserver != null) {
            mEmptyDataSetAdapter.unregisterDataSetObserver(mDataSetObserver);
            mDataSetObserver = null;
        }
    }

    public static class OnScrollAbsListListener implements AbsListView.OnScrollListener {
        private ILoadSwipeRefresh mILoadSwipeRefresh;
        private AbsListView.OnScrollListener mOnScrollListener;

        public OnScrollAbsListListener(ILoadSwipeRefresh loadSwipeRefresh) {
            mILoadSwipeRefresh = loadSwipeRefresh;
        }

        public OnScrollAbsListListener(ILoadSwipeRefresh loadSwipeRefresh, AbsListView.OnScrollListener onScrollListener) {
            mILoadSwipeRefresh = loadSwipeRefresh;
            mOnScrollListener = onScrollListener;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // 只有在闲置状态情况下检查
            if (scrollState == SCROLL_STATE_IDLE) {
                // 如果满足允许上拉加载、非加载状态中、最后一个显示的 item 与数据源的大小一样，则表示滑动入底部
                if (mILoadSwipeRefresh.isEnabledLoad() && !mILoadSwipeRefresh.isLoading()
                        && view.getLastVisiblePosition() == (view.getCount() - 1)) {
                    mILoadSwipeRefresh.loadData();// 执行上拉加载数据
                }
            }
            if (null != mOnScrollListener)
                mOnScrollListener.onScrollStateChanged(view, scrollState);
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (null != mOnScrollListener)
                mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    private class EmptyDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            updateEmptyViewShown(mEmptyDataSetAdapter == null || mEmptyDataSetAdapter.isEmpty());
        }
    }
}
