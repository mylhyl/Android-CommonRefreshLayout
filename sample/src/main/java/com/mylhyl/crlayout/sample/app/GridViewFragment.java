package com.mylhyl.crlayout.sample.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.mylhyl.crlayout.internal.LoadConfig;
import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshGridView;
import com.mylhyl.crlayout.sample.R;

import java.util.ArrayList;
import java.util.List;


public class GridViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        SwipeRefreshAdapterView.OnListLoadListener, AdapterView.OnItemClickListener {
    private SwipeRefreshGridView swipeRefreshGridView;
    private ArrayAdapter<String> adapter;
    private List<String> objects = new ArrayList<>();
    private int index;
    private int footerIndex = 38;

    public GridViewFragment() {
    }

    public static GridViewFragment newInstance() {
        GridViewFragment fragment = new GridViewFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grid_view_xml, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshGridView = (SwipeRefreshGridView) view.findViewById(R.id.swipeRefresh);
        ImageView emptyView = new ImageView(getContext());
        emptyView.setImageResource(R.mipmap.empty);
        swipeRefreshGridView.setEmptyView(emptyView);
        swipeRefreshGridView.setLoadAnimator(true);
        swipeRefreshGridView.setLoadLayoutResource(R.layout.swipe_refresh_footer);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        swipeRefreshGridView.setOnItemClickListener(this);
        swipeRefreshGridView.setOnListLoadListener(this);
        swipeRefreshGridView.setOnRefreshListener(this);

        LoadConfig loadConfig = swipeRefreshGridView.getLoadConfig();
        loadConfig.setLoadText("加载更多数据...");
        loadConfig.setLoadTextSize(18);
        loadConfig.setLoadTextColor(Color.GREEN);

        for (int i = 0; i < footerIndex; i++) {
            objects.add("Grid数据 = " + i);
        }
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, objects);
        swipeRefreshGridView.setAdapter(adapter);
    }


    @Override
    public void onRefresh() {
        index = 0;
        swipeRefreshGridView.postDelayed(new Runnable() {
            @Override
            public void run() {
                objects.add(0, "下拉 = " + (--index));
                adapter.notifyDataSetChanged();
                swipeRefreshGridView.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onListLoad() {
        ++index;
        swipeRefreshGridView.postDelayed(new Runnable() {
            @Override
            public void run() {
                int count = footerIndex + 15;
                for (int i = footerIndex; i < count; i++) {
                    objects.add("上拉 = " + i);
                }
                footerIndex = count;
                adapter.notifyDataSetChanged();
                swipeRefreshGridView.setLoading(false);
                if (index == 1) {
                    swipeRefreshGridView.setLoadCompleted(true);
                }
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT).show();
    }
}
