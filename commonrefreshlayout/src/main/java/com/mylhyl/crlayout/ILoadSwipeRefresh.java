package com.mylhyl.crlayout;

/**
 * Created by hupei on 2016/5/18.
 */
public interface ILoadSwipeRefresh {

    void setOnListLoadListener(SwipeRefreshAdapterView.OnListLoadListener onListLoadListener);

    void setFooterResource(int resource);

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

    IFooterLayout getFooterLayout();

}
