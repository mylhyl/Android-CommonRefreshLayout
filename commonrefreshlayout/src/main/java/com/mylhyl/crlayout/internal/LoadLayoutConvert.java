package com.mylhyl.crlayout.internal;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 转换自定义上拉加载 View
 * Created by hupei on 2016/5/19.
 */
public final class LoadLayoutConvert extends LoadLayoutBase {

    public LoadLayoutConvert(Context context, View v) {
        super(context, v);
    }

    @Override
    void createFooter(ViewGroup v) {
        //查找 mProgressBar 与 mTextView
        int count = v.getChildCount();
        for (int i = 0; i < count; i++) {
            View childAt = v.getChildAt(i);
            if (childAt instanceof ProgressBar)
                mProgressBar = (ProgressBar) childAt;
            else if (childAt instanceof TextView)
                mTextView = (TextView) childAt;
        }
        setLayoutParams(v.getLayoutParams());
        addView(v);
    }
}
