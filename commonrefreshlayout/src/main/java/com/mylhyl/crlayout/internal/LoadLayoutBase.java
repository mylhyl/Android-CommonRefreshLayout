package com.mylhyl.crlayout.internal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 上拉加载 layout 基类
 * Created by hupei on 2016/5/19.
 */
public abstract class LoadLayoutBase extends LinearLayout implements LoadConfig {

    private String mFooterText;
    private String mCompletedText;

    protected ProgressBar mProgressBar;
    protected TextView mTextView;

    abstract void createFooter(ViewGroup v);

    public LoadLayoutBase(Context context) {
        super(context);
        createFooter(this);
    }

    public LoadLayoutBase(Context context, View v) {
        super(context);
        createFooter((ViewGroup) v);
    }

    protected int pxTdp(int size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources().getDisplayMetrics());
    }

    @Override
    public void setLoadViewHeight(int height) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = pxTdp(height);
        setLayoutParams(params);
    }

    @Override
    public void setLoadText(String text) {
        mFooterText = text;
        if (mTextView != null)
            mTextView.setText(text);
    }

    @Override
    public void setLoadTextSize(float size) {
        if (mTextView != null)
            mTextView.setTextSize(size);
    }

    @Override
    public void setLoadTextColor(@ColorInt int color) {
        if (mTextView != null)
            mTextView.setTextColor(color);
    }

    @Override
    public void setLoadViewBackgroundResource(@DrawableRes int resId) {
        setBackgroundResource(resId);
    }

    @Override
    public void setLoadViewBackgroundColor(@ColorInt int color) {
        setBackgroundColor(color);
    }

    @Override
    public void setProgressBarVisibility(int v) {
        if (mProgressBar != null)
            mProgressBar.setVisibility(v);
    }

    @Override
    public void setIndeterminateDrawable(Drawable d) {
        if (mProgressBar != null)
            mProgressBar.setIndeterminateDrawable(d);
    }

    @Override
    public void setLoadCompletedText(String text) {
        mCompletedText = text;
        if (mTextView != null)
            mTextView.setText(text);
    }

    public String getFooterText() {
        return mFooterText;
    }

    public String getCompletedText() {
        return mCompletedText;
    }
}
