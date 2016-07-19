package com.mylhyl.crlayout.sample.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mylhyl.crlayout.SwipeRefreshWebView;
import com.mylhyl.crlayout.sample.R;


public class WebViewFragment extends Fragment {
    private SwipeRefreshWebView swipeRefreshWebView;

    public WebViewFragment() {
    }

    public static WebViewFragment newInstance() {
        WebViewFragment fragment = new WebViewFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_view_xml, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshWebView = (SwipeRefreshWebView) view.findViewById(R.id.swipeRefresh);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        swipeRefreshWebView.autoRefresh(R.color.colorPrimary);
        WebView scrollView = swipeRefreshWebView.getScrollView();
        scrollView.loadUrl("https://github.com/mylhyl");
        scrollView.setWebViewClient(new SampleWebViewClient());
    }

    private class SampleWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            swipeRefreshWebView.autoRefresh();
            view.loadUrl(url);
            return true;
        }
    }
}
