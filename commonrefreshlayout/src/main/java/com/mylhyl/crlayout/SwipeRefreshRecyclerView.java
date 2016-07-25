package com.mylhyl.crlayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.mylhyl.crlayout.internal.ILoadSwipeRefresh;

/**
 * SwipeRefreshLayout 加 RecyclerView 布局<br>
 * 如须自定义加载框，可继承此类重写 {@link #getLoadLayoutResource()}  getLoadLayoutResource}方法
 * <p> Created by hupei on 2016/5/12.
 */
public class SwipeRefreshRecyclerView extends SwipeRefreshAdapterView<RecyclerView> {
    private RecyclerView.Adapter mEmptyDataSetAdapter;
    private EmptyDataSetObserver mDataSetObserver;

    public SwipeRefreshRecyclerView(Context context) {
        super(context);
    }

    public SwipeRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 为 RecyclerView 设置适配器
     *
     * @param adapter
     */
    public final void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null)
            throw new NullPointerException("mAdapter is null please call CygSwipeRefreshLayout.setAdapter");
        getScrollView().setOnScrollListener(new OnScrollRecyclerViewListener(this));
        getScrollView().setAdapter(adapter);
        registerAdapterDataObserver(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        getScrollView().setLayoutManager(layout);
    }

    @Override
    protected RecyclerView createScrollView(Context context, AttributeSet attrs) {
        RecyclerView recyclerView = new RecyclerView(context, attrs);
        recyclerView.setId(android.R.id.list);
        return recyclerView;
    }

    private void registerAdapterDataObserver(RecyclerView.Adapter adapter) {
        mEmptyDataSetAdapter = adapter;
        if (mEmptyDataSetAdapter != null && mDataSetObserver != null) {
            mEmptyDataSetAdapter.unregisterAdapterDataObserver(mDataSetObserver);
        }
        if (mEmptyDataSetAdapter != null && mDataSetObserver == null) {
            mDataSetObserver = new EmptyDataSetObserver();
            mEmptyDataSetAdapter.registerAdapterDataObserver(mDataSetObserver);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mEmptyDataSetAdapter != null && mDataSetObserver != null) {
            mEmptyDataSetAdapter.unregisterAdapterDataObserver(mDataSetObserver);
            mDataSetObserver = null;
        }
    }

    public static class OnScrollRecyclerViewListener extends RecyclerView.OnScrollListener {
        private ILoadSwipeRefresh mILoadSwipeRefresh;
        private RecyclerView.OnScrollListener mOnScrollListener;

        public OnScrollRecyclerViewListener(ILoadSwipeRefresh loadSwipeRefresh) {
            mILoadSwipeRefresh = loadSwipeRefresh;
        }

        public OnScrollRecyclerViewListener(ILoadSwipeRefresh loadSwipeRefresh, RecyclerView.OnScrollListener onScrollListener) {
            mILoadSwipeRefresh = loadSwipeRefresh;
            mOnScrollListener = onScrollListener;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            // 只有在闲置状态情况下检查
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                // 如果满足允许上拉加载、非加载状态中、最后一个显示的 item 与数据源的大小一样，则表示滑动入底部
                if (!isFirstItemVisible(recyclerView) && mILoadSwipeRefresh.isEnabledLoad()
                        && !mILoadSwipeRefresh.isLoading() && isLastItemVisible(recyclerView)) {
                    mILoadSwipeRefresh.loadData();// 执行上拉加载数据
                }
            }
            if (null != mOnScrollListener)
                mOnScrollListener.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (null != mOnScrollListener)
                mOnScrollListener.onScrolled(recyclerView, dx, dy);
        }

        /**
         * 判断第一个条目是否完全可见
         *
         * @param recyclerView
         * @return
         */
        private boolean isFirstItemVisible(RecyclerView recyclerView) {
            final RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();
            // 如果未设置Adapter或者Adapter没有数据可以下拉刷新
            if (null == adapter || adapter.getItemCount() == 0) {
                return true;
            }
            // 第一个条目完全展示,可以刷新
            if (getFirstVisiblePosition(recyclerView) == 0) {
                return recyclerView.getChildAt(0).getTop() >= recyclerView.getTop();
            }
            return false;
        }

        /**
         * 获取第一个可见子View的位置下标
         *
         * @param recyclerView
         * @return
         */
        private int getFirstVisiblePosition(RecyclerView recyclerView) {
            View firstVisibleChild = recyclerView.getChildAt(0);
            return firstVisibleChild != null ?
                    recyclerView.getChildAdapterPosition(firstVisibleChild) : -1;
        }

        /**
         * 判断最后一个条目是否完全可见
         *
         * @param recyclerView
         * @return
         */
        private boolean isLastItemVisible(RecyclerView recyclerView) {
            final RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();
            // 如果未设置Adapter或者Adapter没有数据可以上拉刷新
            if (null == adapter || adapter.getItemCount() == 0) {
                return true;
            }
            // 最后一个条目View完全展示,可以刷新
            int lastVisiblePosition = getLastVisiblePosition(recyclerView);
            if (lastVisiblePosition >= recyclerView.getAdapter().getItemCount() - 1) {
                return recyclerView.getChildAt(recyclerView.getChildCount() - 1).getBottom()
                        <= recyclerView.getBottom();
            }
            return false;
        }

        /**
         * 获取最后一个可见子View的位置下标
         *
         * @param recyclerView
         * @return
         */
        private int getLastVisiblePosition(RecyclerView recyclerView) {
            View lastVisibleChild = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
            return lastVisibleChild != null ? recyclerView.getChildAdapterPosition(lastVisibleChild) : -1;
        }
    }

    private class EmptyDataSetObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            updateEmptyViewShown(mEmptyDataSetAdapter == null || mEmptyDataSetAdapter.getItemCount() == 0);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            onChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            onChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            onChanged();
        }
    }
}
