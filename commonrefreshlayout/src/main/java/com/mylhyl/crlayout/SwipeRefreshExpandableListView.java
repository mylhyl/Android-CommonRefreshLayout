package com.mylhyl.crlayout;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 * SwipeRefreshLayout 包含 ExpandableListView 布局<br>
 * 如须自定义加载框，可继承此类重写 {@link #getLoadLayoutResource()}  getLoadLayoutResource}
 * <p>Created by hupei on 2016/5/16.
 */
public class SwipeRefreshExpandableListView extends SwipeRefreshAbsListView<ExpandableListView> {
    private ExpandableListAdapter mAdapter;

    public SwipeRefreshExpandableListView(Context context) {
        super(context);
    }

    public SwipeRefreshExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 为 ExpandableListView 设置适配器
     *
     * @param adapter
     */
    public final void setAdapter(ExpandableListAdapter adapter) {
        mAdapter = adapter;
        if (mAdapter == null)
            throw new NullPointerException("mAdapter is null please call SwipeRefreshExpandableListView.setAdapter");
        getScrollView().setOnScrollListener(new OnScrollAbsListListener(this));
        getScrollView().setAdapter(mAdapter);
        registerDataSetObserver(new ExpandableListConnector(mAdapter));
    }

    @Override
    protected ExpandableListView createScrollView(Context context, AttributeSet attrs) {
        ExpandableListView expandableListView = new ExpandableListView(context, attrs);
        expandableListView.setId(android.R.id.list);
        return expandableListView;
    }

    private class ExpandableListConnector extends BaseAdapter {
        private ExpandableListAdapter mExpandableListAdapter;

        public ExpandableListConnector(ExpandableListAdapter expandableListAdapter) {
            this.mExpandableListAdapter = expandableListAdapter;
        }

        @Override
        public int getCount() {
            if (mExpandableListAdapter == null) return 0;
            return mExpandableListAdapter.getGroupCount();
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            mExpandableListAdapter.registerDataSetObserver(observer);
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            mExpandableListAdapter.unregisterDataSetObserver(observer);
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
