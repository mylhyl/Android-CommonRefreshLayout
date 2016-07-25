package com.mylhyl.crlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * SwipeRefreshLayout 包含 ListView 布局<br>
 * 如须自定义加载框，可继承此类重写 {@link #getLoadLayoutResource()}  getLoadLayoutResource}方法
 * <p>Created by hupei on 2016/5/12.
 */
public class SwipeRefreshListView extends SwipeRefreshAbsListView<ListView> {

    public SwipeRefreshListView(Context context) {
        super(context);
    }

    public SwipeRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected ListView createScrollView(Context context, AttributeSet attrs) {
        ListView listView = new ListView(context, attrs);
        listView.setId(android.R.id.list);
        return listView;
    }
}
