package com.mylhyl.crlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.mylhyl.crlayout.internal.FooterLayout;
import com.mylhyl.crlayout.internal.FooterLayoutConvert;

public abstract class SwipeRefreshAdapterView<T extends View> extends BaseSwipeRefresh<T> implements ILoadSwipeRefresh {

    private static final String DEFAULT_FOOTER_TEXT = "加载数据中...";

    private boolean mLoading;// 是否处于上拉加载状态中
    private boolean mEnabledLoad;// 是否允许上拉加载
    private View mFooterView;
    private int mFooterResource;
    private String mFooterText;
    private Drawable mFooterIndeterminateDrawable;

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
                mFooterResource = a.getResourceId(R.styleable.crLayout_footer_layout, 0);
                mFooterText = a.getString(R.styleable.crLayout_footer_text);
                mFooterIndeterminateDrawable = a.getDrawable(R.styleable.crLayout_footer_indeterminate_drawable);

                a.recycle();
            }
        }
        if (TextUtils.isEmpty(mFooterText))
            mFooterText = DEFAULT_FOOTER_TEXT;

    }

    @Override
    public final void setOnListLoadListener(OnListLoadListener onListLoadListener) {
        this.mOnListLoadListener = onListLoadListener;
        setEnabledLoad(mOnListLoadListener != null);
        addFooterView();
    }

    private void addFooterView() {
        if (mFooterView == null && mOnListLoadListener != null) {
            createFooter();//创建上拉加载 View
            if (mFooterView == null)
                throw new NullPointerException("method onCreateFooterView cannot return null");
            //如是自定义 FooterView 的，则转换
            boolean b = mFooterView instanceof IFooterLayout;
            if (!b) {
                mFooterView = new FooterLayoutConvert(getContext(), mFooterView);
            }
            addView(mFooterView);
            hideFooter();
        }
    }

    private void createFooter() {
        if ((mFooterResource = getFooterResource()) > 0) {
            mFooterView = LayoutInflater.from(getContext()).inflate(mFooterResource, this, false);
        } else {
            FooterLayout footerLayout = new FooterLayout(getContext());
            footerLayout.setFooterText(mFooterText);
            if (mFooterIndeterminateDrawable != null)
                footerLayout.setIndeterminateDrawable(mFooterIndeterminateDrawable);
            mFooterView = footerLayout;
        }
    }

    @Override
    public final void setFooterResource(int resource) {
        mFooterResource = resource;
    }

    /**
     * 获取自定义上拉加载 layoutResource，子类可重写
     *
     * @return
     */
    protected int getFooterResource() {
        return mFooterResource;
    }


    @Override
    public final void loadData() {
        if (mOnListLoadListener != null) {
            setLoading(true);// 设置状态
            mOnListLoadListener.onListLoad();
        }
    }

    @Override
    public final void setLoading(boolean loading) {
        this.mLoading = loading;
        if (mLoading) showFooter();
        else hideFooter();
    }

    private void showFooter() {
        if (mFooterView != null)
            mFooterView.setVisibility(VISIBLE);
    }

    private void hideFooter() {
        if (mFooterView != null)
            mFooterView.setVisibility(GONE);
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
    public IFooterLayout getFooterLayout() {
        if (mFooterView == null)
            throw new NullPointerException("mFooterView is null please call after setOnListLoadListener");
        if (mFooterView instanceof IFooterLayout)
            return (IFooterLayout) mFooterView;
        throw new RuntimeException("mFooterView is no interface IFooterLayout");
    }


    public interface OnListLoadListener {
        /**
         * 当上拉加载时，此方法将被调用
         */
        void onListLoad();
    }
}
