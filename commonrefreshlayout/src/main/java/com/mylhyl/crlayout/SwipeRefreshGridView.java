package com.mylhyl.crlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * SwipeRefreshLayout 包含 HeaderGridView 布局<br>
 * 如须自定义加载框，可继承此类重写 {@link #getLoadLayoutResource()}  getLoadLayoutResource}方法
 * <p>Created by hupei on 2016/5/17.
 */
public class SwipeRefreshGridView extends SwipeRefreshAbsListView<GridView> {

    @Override
    protected GridView createScrollView(Context context, AttributeSet attrs) {
        GridView gridView = new GridView(context, attrs);
        gridView.setId(android.R.id.list);
        return gridView;
    }

    public SwipeRefreshGridView(Context context) {
        super(context);
    }

    public SwipeRefreshGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}
