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

import com.mylhyl.crlayout.IFooterLayout;

/**
 * 上拉加载 layout 基类
 * Created by hupei on 2016/5/19.
 */
abstract class FooterLayoutBase extends LinearLayout implements IFooterLayout {
    protected ProgressBar mProgressBar;
    protected TextView mTextView;

    abstract void createFooter(ViewGroup v);

    public FooterLayoutBase(Context context) {
        super(context);
        createFooter(this);
    }

    public FooterLayoutBase(Context context, View v) {
        super(context);
        createFooter((ViewGroup) v);
    }

    protected int pxTdp(int size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources().getDisplayMetrics());
    }

    @Override
    public void setFooterHeight(int height) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = pxTdp(height);
        setLayoutParams(params);
    }

    @Override
    public void setFooterText(CharSequence text) {
        if (mTextView != null)
            mTextView.setText(text);
    }

    @Override
    public void setFooterTextSize(float size) {
        if (mTextView != null)
            mTextView.setTextSize(size);
    }

    @Override
    public void setFooterTextColor(@ColorInt int color) {
        if (mTextView != null)
            mTextView.setTextColor(color);
    }

    @Override
    public void setFooterBackgroundResource(@DrawableRes int resId) {
        setBackgroundResource(resId);
    }

    @Override
    public void setFooterBackgroundColor(@ColorInt int color) {
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
}
