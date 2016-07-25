package com.mylhyl.crlayout.internal;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 上拉加载 layout 包含 ProgressBar 与 TextView
 * Created by hupei on 2016/5/19.
 */
public final class LoadLayout extends LoadLayoutBase {
    public LoadLayout(Context context) {
        super(context);
    }

    @Override
    void createFooter(ViewGroup v) {
        LinearLayout root = (LinearLayout) v;
        root.setOrientation(HORIZONTAL);
        root.setGravity(Gravity.CENTER);
        root.setBackgroundColor(Color.WHITE);
        root.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, pxTdp(50)));

        mProgressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmallInverse);
        root.addView(mProgressBar, new AbsListView.LayoutParams(
                AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));

        mTextView = new TextView(getContext());
        mTextView.setTextAppearance(getContext(), android.R.attr.textAppearanceMedium);
        root.addView(mTextView, new LayoutParams(
                AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
    }
}
