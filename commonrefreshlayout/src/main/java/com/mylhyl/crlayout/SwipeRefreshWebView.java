package com.mylhyl.crlayout;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * SwipeRefreshLayout 加 WebView 布局<br>
 * <p> Created by hupei on 2016/5/12.
 */
public class SwipeRefreshWebView extends BaseSwipeRefresh<WebView> {
    private final SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getScrollView().reload();
        }
    };
    private final WebChromeClient defaultWebChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                setRefreshing(false);
            }
        }
    };

    public SwipeRefreshWebView(Context context) {
        this(context, null);
    }

    public SwipeRefreshWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnRefreshListener(refreshListener);
        getScrollView().setWebChromeClient(defaultWebChromeClient);
    }

    @Override
    public final void autoRefresh() {
        mLoadSwipeRefresh.autoRefresh();
        mLoadSwipeRefresh.setRefreshing(true);
    }

    @Override
    public final void autoRefresh(@ColorRes int... colorResIds) {
        //重写主要是为了显示动画时，不响应 onRefresh() 方法
        mLoadSwipeRefresh.autoRefresh(colorResIds);
        mLoadSwipeRefresh.setRefreshing(true);
    }

    @Override
    protected WebView createScrollView(Context context, AttributeSet attrs) {
        WebView webView = new WebView(context, attrs);
        webView.setId(android.R.id.list);
        return webView;
    }
}
