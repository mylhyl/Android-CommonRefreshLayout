package com.mylhyl.crlayout;

import android.graphics.drawable.Drawable;

/**
 * Created by hupei on 2016/5/19.
 */
public interface IFooterLayout {
    /**
     * 设置高
     *
     * @param height
     */
    void setFooterHeight(int height);

    /**
     * 设置文字
     *
     * @param label
     */
    void setFooterText(CharSequence label);

    /**
     * 设置文字大小
     *
     * @param size
     */
    void setFooterTextSize(float size);

    /**
     * 设置文字颜色
     *
     * @param color
     */
    void setFooterTextColor(int color);

    /**
     * 设置资源背景
     *
     * @param resId
     */
    void setFooterBackgroundResource(int resId);

    /**
     * 设置颜色背景
     *
     * @param color
     */
    void setFooterBackgroundColor(int color);

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
}
