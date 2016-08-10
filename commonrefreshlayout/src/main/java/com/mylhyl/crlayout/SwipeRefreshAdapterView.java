package com.mylhyl.crlayout;

import android.animation.Animator;
import android.animation.ValueAnimator;
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

    private ValueAnimator mShowLoadAnimator;
    private ValueAnimator mHideLoadAnimator;
    private int mLoadLayoutHeight;
    private boolean isLoadAnimator;

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
                isLoadAnimator = a.getBoolean(R.styleable.crLayout_load_animator, false);
                a.recycle();
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (isLoadAnimator && mLoadLayout != null && mLoadLayoutHeight == 0) {
            mLoadLayoutHeight = mLoadLayout.getMeasuredHeight();
            if (mLoadLayoutHeight > 0) {
                mLoadLayout.setVisibility(GONE);
                initShowLoadAnimator();
                initHideLoadAnimator();
            }
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    private void initShowLoadAnimator() {
        mShowLoadAnimator = ValueAnimator.ofFloat(0, mLoadLayoutHeight);
        mShowLoadAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LayoutParams lp = (LayoutParams) mLoadLayout.getLayoutParams();
                lp.height = (int) ((Float) animation.getAnimatedValue()).floatValue();
                mLoadLayout.setLayoutParams(lp);
                getScrollView().setTranslationY(-lp.height);
            }
        });
        mShowLoadAnimator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mOnListLoadListener != null && !isLoadCompleted) {
                    mOnListLoadListener.onListLoad();
                }
            }
        });
        mShowLoadAnimator.setDuration(300);
    }

    private void initHideLoadAnimator() {
        mHideLoadAnimator = ValueAnimator.ofFloat(mLoadLayoutHeight, 0);
        mHideLoadAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LayoutParams lp = (LayoutParams) mLoadLayout.getLayoutParams();
                lp.height = (int) ((Float) animation.getAnimatedValue()).floatValue();
                mLoadLayout.setLayoutParams(lp);
                getScrollView().setTranslationY(-lp.height);
            }
        });
        mHideLoadAnimator.setDuration(300);
    }

    @Override
    public final void setOnListLoadListener(OnListLoadListener onListLoadListener) {
        this.mOnListLoadListener = onListLoadListener;
        setEnabledLoad(mOnListLoadListener != null);
        if (mEnabledLoad) addLoadLayout();
    }

    private void addLoadLayout() {
        if (mLoadLayout == null) {
            createLoadLayout();//创建上拉加载 View
            LayoutParams layoutParams = new LayoutParams(mLoadLayout.getLayoutParams());
            layoutParams.gravity = Gravity.BOTTOM;
            addView(mLoadLayout, layoutParams);
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
        if (mLoadLayout == null)
            throw new NullPointerException("mLoadLayout is null");

        if (!isLoadAnimator) mLoadLayout.setVisibility(GONE);

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
        if (mLoadLayout != null)
            throw new RuntimeException("please call setLoadLayoutResource before setOnListLoadListener");
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
        setLoading(true);
    }

    @Override
    public final void setLoading(boolean loading) {
        if (mLoading == loading) return;
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
            mLoadLayout.setVisibility(View.VISIBLE);

            if (isLoadAnimator && mShowLoadAnimator != null) mShowLoadAnimator.start();
            else if (!isLoadAnimator && !isLoadCompleted && mOnListLoadListener != null)
                mOnListLoadListener.onListLoad();
        }
    }

    private void hideLoadLayout() {
        if (isLoadAnimator && mHideLoadAnimator != null) mHideLoadAnimator.start();
        else mLoadLayout.setVisibility(GONE);
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
        if (mLoadLayout == null) addLoadLayout();
        return mLoadLayout;
    }

    @Override
    public void setLoadAnimator(boolean loadAnimator) {
        isLoadAnimator = loadAnimator;
        if (mLoadLayout != null)
            throw new RuntimeException("please call setLoadAnimator before setOnListLoadListener");
    }

    public interface OnListLoadListener {
        /**
         * 当上拉加载时，此方法将被调用
         */
        void onListLoad();
    }

    static class SimpleAnimatorListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
