package com.mylhyl.crlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mylhyl.crlayout.internal.ISwipeRefresh;

/**
 * android v4 兼容包 中 SwipeRefreshLayout 刷新控件，支持上拉加载
 * <p/>
 * <pre>
 * 此处主要封装以下几点：
 * 1.手动刷新 {@link #autoRefresh() autoRefresh}
 * 2.支持上拉加载，并可自定义
 * 3.解决 SwipeRefreshLayout 与可滑动控件使用过程中冲突的问题
 * </pre>
 * Created by hupei on 2016/5/12.
 */
public abstract class BaseSwipeRefresh<T extends View> extends FrameLayout implements ISwipeRefresh {

    /**
     * 创建可滑动 View，子类实现
     *
     * @param context
     * @param attrs
     * @return
     */
    protected abstract T createScrollView(Context context, AttributeSet attrs);

    private FrameLayout mRefreshLayoutWrapper;
    protected MultiSwipeRefreshLayout mLoadSwipeRefresh;
    private T mScrollView;
    private View mEmptyView;
    private String mEmptyViewText = "";
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;

    protected boolean isLoadCompleted;

    public BaseSwipeRefresh(Context context) {
        this(context, null);
    }

    public BaseSwipeRefresh(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseSwipeRefresh(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseSwipeRefresh(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        int[] crLayout = R.styleable.crLayout;
        if (crLayout != null && crLayout.length > 0) {
            final TypedArray a = context.obtainStyledAttributes(attrs, crLayout, defStyleAttr, defStyleRes);
            if (a != null) {
                mEmptyViewText = a.getString(R.styleable.crLayout_empty_text);
                a.recycle();
            }
        }
        mLoadSwipeRefresh = new MultiSwipeRefreshLayout(context, attrs);
        mScrollView = createScrollView(context, attrs);
        mEmptyView = createEmptyView(context);
        addSwipeRefreshView(context, mLoadSwipeRefresh, mScrollView, mEmptyView);
    }

    @SuppressWarnings("ResourceType")
    private View createEmptyView(Context context) {
        TextView emptyView = new TextView(context);
        emptyView.setId(android.R.id.empty);
        emptyView.setTextAppearance(context, android.R.attr.textAppearanceSmall);
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setVisibility(View.GONE);
        emptyView.setText(mEmptyViewText);
        return emptyView;
    }

    private void addSwipeRefreshView(Context context, MultiSwipeRefreshLayout loadSwipeRefresh, T scrollView, View emptyView) {
        mRefreshLayoutWrapper = new FrameLayout(context);
        mRefreshLayoutWrapper.addView(scrollView,
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mRefreshLayoutWrapper.addView(emptyView,
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        loadSwipeRefresh.addView(mRefreshLayoutWrapper, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        addView(loadSwipeRefresh, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1));
        loadSwipeRefresh.setSwipeableChildren(scrollView, emptyView);
    }

    @Override
    public void autoRefresh() {
        mLoadSwipeRefresh.autoRefresh();
        this.setRefreshing(true);
    }

    @Override
    public void autoRefresh(@ColorRes int... colorResIds) {
        mLoadSwipeRefresh.autoRefresh(colorResIds);
        this.setRefreshing(true);
    }

    @Override
    public final void autoRefresh(boolean scale, int start, int end, @ColorRes int... colorResIds) {
        mLoadSwipeRefresh.autoRefresh(scale, start, end, colorResIds);
        this.setRefreshing(true);
    }

    @Override
    public final void setEmptyText(int resId) {
        setEmptyText(getContext().getString(resId));
    }

    @Override
    public final void setEmptyText(CharSequence text) {
        if (mEmptyView != null && mEmptyView instanceof TextView) {
            TextView textView = (TextView) mEmptyView;
            textView.setText(text);
        }
    }

    @Override
    public final void setEmptyView(View newEmptyView) {
        if (null != newEmptyView) {
            newEmptyView.setVisibility(GONE);
            mEmptyView = newEmptyView;
            newEmptyView.setClickable(true);

            ViewParent newEmptyViewParent = newEmptyView.getParent();
            if (null != newEmptyViewParent && newEmptyViewParent instanceof ViewGroup) {
                ((ViewGroup) newEmptyViewParent).removeView(newEmptyView);
            }
            FrameLayout.LayoutParams lp = convertEmptyViewLayoutParams(newEmptyView.getLayoutParams());
            if (null != lp) {
                mRefreshLayoutWrapper.addView(newEmptyView, lp);
            } else {
                mRefreshLayoutWrapper.addView(newEmptyView);
            }
        }
    }

    private static FrameLayout.LayoutParams convertEmptyViewLayoutParams(ViewGroup.LayoutParams lp) {
        FrameLayout.LayoutParams newLp = null;
        if (null != lp) {
            newLp = new FrameLayout.LayoutParams(lp);
            if (lp instanceof LinearLayout.LayoutParams)
                newLp.gravity = ((LinearLayout.LayoutParams) lp).gravity;
            else newLp.gravity = Gravity.CENTER;
        }
        return newLp;
    }

    @Override
    public final View getEmptyView() {
        return mEmptyView;
    }

    final void updateEmptyViewShown(boolean shown) {
        /*
         * ListView不能设置隐藏，否则下拉刷新动画无法跟随手势，因为 SwipeRefreshLayout 必须有滚动的view。
		 * mListView.setVisibility(!shown ? View.VISIBLE : View.GONE);
		 */
        if (mEmptyView != null)
            mEmptyView.setVisibility(shown ? View.VISIBLE : View.GONE);
    }

    @Override
    public final void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        mOnRefreshListener = listener;
        mLoadSwipeRefresh.setOnRefreshListener(listener);
    }

    @Override
    public final void setRefreshing(boolean refreshing) {
        isLoadCompleted = false;
        mLoadSwipeRefresh.setRefreshing(refreshing);
        // 下拉刷新中，此处目的是为了手动调用 setRefreshing(true)时，响应下拉刷新事件
        if (mLoadSwipeRefresh.isRefreshing() && mOnRefreshListener != null)
            mOnRefreshListener.onRefresh();
    }

    @Override
    public void setEnabled(boolean enabled) {
        getSwipeRefreshLayout().setEnabled(enabled);
    }

    @Override
    public final T getScrollView() {
        return mScrollView;
    }

    @Override
    public final SwipeRefreshLayout getSwipeRefreshLayout() {
        return mLoadSwipeRefresh;
    }
}
