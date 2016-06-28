package com.mylhyl.crlayout;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

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
            throw new NullPointerException("mAdapter is null please call CygSwipeRefreshLayout.setAdapter");
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
    public class OnScrollAbsListListener implements AbsListView.OnScrollListener {
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
            if (null != mOnScrollListener)
                mOnScrollListener.onScrollStateChanged(view, scrollState);
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem > 0
                    && mILoadSwipeRefresh.isEnabledLoad() && !mILoadSwipeRefresh.isLoading()
                    && view.getLastVisiblePosition() == (totalItemCount - 1)) {
                mILoadSwipeRefresh.loadData();// 滑动底部自动执行上拉加载
            }

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
