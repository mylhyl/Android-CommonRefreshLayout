package com.mylhyl.crlayout.sample.app;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mylhyl.crlayout.SwipeRefreshExpandableListView;
import com.mylhyl.crlayout.app.SwipeRefreshExpandableListFragment;
import com.mylhyl.crlayout.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hupei on 2016/5/13.
 */
public class AppExpandableListFragment extends SwipeRefreshExpandableListFragment {
    private MyExpandableListAdapter adapter;
    private List<String> groups = new ArrayList<>();
    private List<List<String>> childs = new ArrayList<>();
    private int index;
    private int footerIndex = 20;

    public static AppExpandableListFragment newInstance() {
        return new AppExpandableListFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        setEnabledLoad(true);
        for (int i = 0; i < footerIndex; i++) {
            groups.add("group = " + i);
            List<String> child = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                child.add("child = " + i + ":" + j);
            }
            childs.add(child);
        }
        adapter = new MyExpandableListAdapter(this.getContext());
        setListAdapter(adapter);
        setEmptyText("什么也没有！");
    }

    @Override
    public SwipeRefreshExpandableListView createSwipeRefreshLayout() {
        return new SwipeRefreshExpandableListView(getActivity());
    }

    @Override
    public boolean onListGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        Toast.makeText(getContext(), "groupPosition：" + groupPosition, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onListChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {
        Toast.makeText(getContext(), "groupPosition："
                + groupPosition + " | childPosition：" + childPosition, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onRefresh() {
        getSwipeRefreshLayout().getScrollView().postDelayed(new Runnable() {
            @Override
            public void run() {
                int count = --index;
                groups.add(0, "下拉group = " + count);
                List<String> child = new ArrayList<>();
                for (int j = 0; j < 3; j++) {
                    child.add("下拉child = " + count + ":" + j);
                }
                childs.add(child);
                adapter.notifyDataSetChanged();
                setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onListLoad() {
        getSwipeRefreshLayout().getScrollView().postDelayed(new Runnable() {
            @Override
            public void run() {
                int count = footerIndex + 3;
                for (int i = footerIndex; i < count; i++) {
                    groups.add("上拉group = " + count);
                    List<String> child = new ArrayList<>();
                    for (int j = 0; j < 3; j++) {
                        child.add("上拉child = " + count + ":" + j);
                    }
                    childs.add(0, child);
                }
                footerIndex = count;
                adapter.notifyDataSetChanged();
                setLoading(false);
            }
        }, 2000);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        groups.clear();
        childs.clear();
        adapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    class MyExpandableListAdapter extends BaseExpandableListAdapter {
        private LayoutInflater inflater;

        public MyExpandableListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getGroupCount() {
            return groups.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childs.get(groupPosition).size();
        }

        @Override
        public String getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        @Override
        public String getChild(int groupPosition, int childPosition) {
            return childs.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view = inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
            TextView tv = (TextView) view.findViewById(android.R.id.text1);
            tv.setText(getGroup(groupPosition));
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            View view = inflater.inflate(android.R.layout.simple_expandable_list_item_2, parent, false);
            TextView tv = (TextView) view.findViewById(android.R.id.text1);
            tv.setText(getChild(groupPosition, childPosition));
            tv.setTextColor(Color.RED);
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
