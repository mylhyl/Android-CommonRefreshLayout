package com.mylhyl.crlayout.sample.app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.mylhyl.crlayout.IFooterLayout;
import com.mylhyl.crlayout.app.SwipeRefreshListFragment;
import com.mylhyl.crlayout.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hupei on 2016/5/13.
 */
public class AppListFragment extends SwipeRefreshListFragment {
    private ArrayAdapter<String> adapter;
    private List<String> objects = new ArrayList<>();
    private int index;
    private int footerIndex = 20;

    public static AppListFragment newInstance() {
        return new AppListFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        //注意在 setOnListLoadListener 或 setEnabledLoad 之前使用
        getSwipeRefreshLayout().setFooterResource(R.layout.swipe_refresh_footer);
        setEnabledLoad(true);

        IFooterLayout footerLayout = getSwipeRefreshLayout().getFooterLayout();
        footerLayout.setFooterText("加载更多数据...");
        footerLayout.setFooterTextSize(18);
        footerLayout.setFooterTextColor(Color.GREEN);

        getSwipeRefreshLayout().autoRefresh();

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, objects);
        setListAdapter(adapter);
        setEmptyText("无数据");
    }

    @Override
    public void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onListItemClick(parent, view, position, id);
        Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        getSwipeRefreshLayout().getScrollView().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adapter.isEmpty()) {
                    for (int i = 0; i < footerIndex; i++) {
                        objects.add("数据 = " + i);
                    }
                } else {
                    objects.add(0, "下拉 = " + (--index));
                }
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
                int count = footerIndex + 5;
                for (int i = footerIndex; i < count; i++) {
                    objects.add("上拉 = " + i);
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
        objects.clear();
        adapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }
}
