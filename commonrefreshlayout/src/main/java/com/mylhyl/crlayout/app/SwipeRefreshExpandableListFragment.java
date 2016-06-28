package com.mylhyl.crlayout.app;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.mylhyl.crlayout.SwipeRefreshExpandableListView;

/**
 * Created by hupei on 2016/5/16.
 */
public abstract class SwipeRefreshExpandableListFragment extends SwipeRefreshAbsListFragment<SwipeRefreshExpandableListView> {
    private final ExpandableListView.OnGroupClickListener mOnGroupClickListener = new ExpandableListView.OnGroupClickListener() {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            return onListGroupClick(parent, v, groupPosition, id);
        }
    };
    private final ExpandableListView.OnChildClickListener mOnChildClickListener = new ExpandableListView.OnChildClickListener() {

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            return onListChildClick(parent, v, groupPosition, childPosition, id);
        }
    };

    public boolean onListChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return false;
    }

    public boolean onListGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return false;
    }

    @Override
    public final void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
        //子类不允许重写
        super.onListItemClick(parent, view, position, id);
    }

    @Override
    public SwipeRefreshExpandableListView createSwipeRefreshLayout() {
        return new SwipeRefreshExpandableListView(getActivity());
    }

    public final void setListAdapter(ExpandableListAdapter adapter) {
        ExpandableListView expandableListView = getSwipeRefreshLayout().getScrollView();
        SwipeRefreshExpandableListView swipeRefreshExpandableListView = getSwipeRefreshLayout();
        if (expandableListView != null && swipeRefreshExpandableListView != null) {
            expandableListView.setVisibility(View.VISIBLE);

            expandableListView.setOnGroupClickListener(mOnGroupClickListener);
            expandableListView.setOnChildClickListener(mOnChildClickListener);

            swipeRefreshExpandableListView.setAdapter(adapter);
        }
    }
}
