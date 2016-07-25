package com.mylhyl.crlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.mylhyl.crlayout.internal.ILoadSwipeRefresh;
import com.mylhyl.crlayout.internal.LoadConfig;
import com.mylhyl.crlayout.internal.LoadLayout;
import com.mylhyl.crlayout.internal.LoadLayoutBase;
import com.mylhyl.crlayout.internal.LoadLayoutConvert;

public abstract class SwipeRefreshAdapterView<T extends View> extends BaseSwipeRefresh<T> implements ILoadSwipeRefresh {

    private static final String DEFAULT_FOOTER_TEXT = "加载数据中...";
    private static final String DEFAULT_FOOTER_COMPLETED_TEXT = "没有更多了";

    private boolean mLoading;// 是否处于上拉加载状态中
    private boolean mEnabledLoad;// 是否允许上拉加载
    private LoadLayoutBase mLoadLayout;
    private int mLoadResource;
    private String mLoadText;
    private String mCompletedText;
    private Drawable mLoadIndeterminateDrawable;

    private OnListLoadListener mOnListLoadListener;

    public SwipeRefreshAdapterView(Context context) {
        this(context, null);
    }

    public SwipeRefreshAdapterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRefreshAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwipeRefreshAdapterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        int[] crLayout = R.styleable.crLayout;
        if (crLayout != null && crLayout.length > 0) {
            final TypedArray a = context.obtainStyledAttributes(attrs, crLayout, defStyleAttr, defStyleRes);
            if (a != null) {
                mLoadResource = a.getResourceId(R.styleable.crLayout_load_layout, 0);
                mLoadText = a.getString(R.styleable.crLayout_load_text);
                mLoadIndeterminateDrawable = a.getDrawable(R.styleable.crLayout_load_indeterminate_drawable);
                mCompletedText = a.getString(R.styleable.crLayout_load_completed_text);
                a.recycle();
            }
        }
    }

    @Override
    public final void setOnListLoadListener(OnListLoadListener onListLoadListener) {
        this.mOnListLoadListener = onListLoadListener;
        setEnabledLoad(mOnListLoadListener != null);
        addLoadLayout();
    }

    private void addLoadLayout() {
        if (mLoadLayout == null && mOnListLoadListener != null) {
            createLoadLayout();//创建上拉加载 View
            if (mLoadLayout == null)
                throw new NullPointerException("method onCreateFooterView cannot return null");
            LayoutParams layoutParams = new LayoutParams(mLoadLayout.getLayoutParams());
            layoutParams.gravity = Gravity.BOTTOM;
            addView(mLoadLayout, layoutParams);
            hideLoadLayout();
        }
    }

    private void createLoadLayout() {
        if ((mLoadResource = getLoadLayoutResource()) > 0) {
            View view = LayoutInflater.from(getContext()).inflate(mLoadResource, this, false);
            mLoadLayout = new LoadLayoutConvert(getContext(), view);
        } else {
            LoadLayout footerLayout = new LoadLayout(getContext());
            mLoadLayout = footerLayout;
        }

        if (TextUtils.isEmpty(mCompletedText))
            mCompletedText = DEFAULT_FOOTER_COMPLETED_TEXT;
        mLoadLayout.setLoadCompletedText(mCompletedText);

        if (TextUtils.isEmpty(mLoadText))
            mLoadText = DEFAULT_FOOTER_TEXT;
        mLoadLayout.setLoadText(mLoadText);

        if (mLoadIndeterminateDrawable != null)
            mLoadLayout.setIndeterminateDrawable(mLoadIndeterminateDrawable);
    }

    @Override
    public final void setLoadLayoutResource(int resource) {
        mLoadResource = resource;
    }

    /**
     * 获取自定义上拉加载 layoutResource，子类可重写
     *
     * @return
     */
    protected int getLoadLayoutResource() {
        return mLoadResource;
    }


    @Override
    public final void loadData() {
        if (mOnListLoadListener != null) {
            setLoading(true);// 设置状态
            if (!isLoadCompleted)
                mOnListLoadListener.onListLoad();
        }
    }

    @Override
    public final void setLoading(boolean loading) {
        this.mLoading = loading;
        if (mLoading) showLoadLayout();
        else hideLoadLayout();
    }

    private void showLoadLayout() {
        if (mLoadLayout != null) {
            LoadConfig footerLayout = getLoadConfig();
            if (isLoadCompleted) {
                footerLayout.setLoadCompletedText(mLoadLayout.getCompletedText());
                footerLayout.setProgressBarVisibility(View.GONE);
                mLoadLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setLoading(false);
                    }
                }, 1000);
            } else {
                footerLayout.setLoadText(mLoadLayout.getFooterText());
                footerLayout.setProgressBarVisibility(View.VISIBLE);
            }
            mLoadLayout.setVisibility(VISIBLE);
        }
    }

    private void hideLoadLayout() {
        if (mLoadLayout != null)
            mLoadLayout.setVisibility(GONE);
    }

    @Override
    public final void setEnabledLoad(boolean enabled) {
        mEnabledLoad = enabled;
    }

    @Override
    public final boolean isLoading() {
        return mLoading;
    }

    @Override
    public final boolean isEnabledLoad() {
        return mEnabledLoad;
    }

    @Override
    public void setLoadCompleted(boolean loadCompleted) {
        isLoadCompleted = loadCompleted;
    }

    @Override
    public LoadConfig getLoadConfig() {
        if (mLoadLayout == null)
            throw new NullPointerException("mLoadLayout is null please call after setOnListLoadListener");
        return mLoadLayout;
    }


    public interface OnListLoadListener {
        /**
         * 当上拉加载时，此方法将被调用
         */
        void onListLoad();
    }
}
