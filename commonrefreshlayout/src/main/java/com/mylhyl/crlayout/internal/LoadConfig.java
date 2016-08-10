package com.mylhyl.crlayout.internal;

import android.graphics.drawable.Drawable;

/**
 * Created by hupei on 2016/5/19.
 */
public interface LoadConfig {
    /**
     * 设置加载文字
     *
     * @param label
     */
    void setLoadText(String label);

    /**
     * 设置加载文字大小
     *
     * @param size
     */
    void setLoadTextSize(float size);

    /**
     * 设置加载文字颜色
     *
     * @param color
     */
    void setLoadTextColor(int color);

    /**
     * 设置菊花显示或隐藏
     *
     * @param v
     */
    void setProgressBarVisibility(int v);

    /**
     * 设置菊花样式
     *
     * @param d
     */
    void setIndeterminateDrawable(Drawable d);

    /**
     * 设置加载完成文字
     *
     * @param text
     */
    void setLoadCompletedText(String text);

    /**
     * 设置加载更多 Layout 高度
     *
     * @param height
     */
    void setLoadViewHeight(int height);

    /**
     * 设置加载更多 Layout 资源背景
     *
     * @param resId
     */
    void setLoadViewBackgroundResource(int resId);

    /**
     * 设置加载更多 Layout 颜色背景
     *
     * @param color
     */
    void setLoadViewBackgroundColor(int color);
}
