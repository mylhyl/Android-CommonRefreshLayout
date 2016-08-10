package com.mylhyl.crlayout.internal;

import com.mylhyl.crlayout.SwipeRefreshAdapterView;

/**
 * Created by hupei on 2016/5/18.
 */
public interface ILoadSwipeRefresh {

    void setOnListLoadListener(SwipeRefreshAdapterView.OnListLoadListener onListLoadListener);

    void setLoadLayoutResource(int resource);

    void loadData();

    /**
     * 设置是否处于上拉加载状态
     *
     * @param loading 为true加载状态，false结束加载
     */
    void setLoading(boolean loading);

    /**
     * 允许上拉加载
     *
     * @param enabled
     */
    void setEnabledLoad(boolean enabled);

    /**
     * 是否在上拉加载中
     *
     * @return
     */
    boolean isLoading();

    /**
     * 是否允许上拉加载
     *
     * @return
     */
    boolean isEnabledLoad();

    /**
     * 设置加载完成
     *
     * @param loadCompleted
     */
    void setLoadCompleted(boolean loadCompleted);

    LoadConfig getLoadConfig();

    /**
     * 设置加载Layout有动画显示效果,默认false
     *
     * @param isLoadAnimator true 启用动画
     */
    void setLoadAnimator(boolean isLoadAnimator);
}
