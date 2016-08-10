package com.mylhyl.crlayout.sample.app;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mylhyl.crlayout.SwipeRefreshAdapterView;
import com.mylhyl.crlayout.SwipeRefreshListView;
import com.mylhyl.crlayout.internal.LoadConfig;
import com.mylhyl.crlayout.sample.R;

import java.util.ArrayList;
import java.util.List;


public class ListViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        SwipeRefreshAdapterView.OnListLoadListener, AdapterView.OnItemClickListener {
    private SwipeRefreshListView swipeRefreshListView;
    private ArrayAdapter<String> adapter;
    private List<String> objects = new ArrayList<>();
    private int index;
    private int pagerSize = 20;

    public ListViewFragment() {
    }

    public static ListViewFragment newInstance() {
        ListViewFragment fragment = new ListViewFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_view_xml, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshListView = (SwipeRefreshListView) view.findViewById(R.id.swipeRefresh);
        swipeRefreshListView.getSwipeRefreshLayout().setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshListView.setEnabled(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        swipeRefreshListView.setOnItemClickListener(this);
        swipeRefreshListView.setOnListLoadListener(this);
        swipeRefreshListView.setOnRefreshListener(this);

        initListViewHead();

        LoadConfig loadConfig = swipeRefreshListView.getLoadConfig();
        loadConfig.setLoadViewBackgroundColor(getResources().getColor(android.R.color.darker_gray));
//        swipeRefreshListView.setEmptyText("数据呢？");

        for (int i = 0; i < pagerSize; i++) {
            objects.add("数据 = " + i);
        }
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, objects);
        swipeRefreshListView.setAdapter(adapter);

    }

    private int mCurrentPosition;
    private ViewPager mViewPager;
    private final Handler mAutoPlayHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mCurrentPosition++;
            if (mViewPager != null)
                mViewPager.setCurrentItem(mCurrentPosition);
            mAutoPlayHandler.sendEmptyMessageDelayed(1000, 3000);
        }
    };

    private void initListViewHead() {
        if (mViewPager == null) {
            mViewPager = new ViewPager(getContext());
            mViewPager.setLayoutParams(new AbsListView.LayoutParams(
                    AbsListView.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics())));
        }
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 4 + 2;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                int toRealPosition = toRealPosition(position);
                TextView textView = (TextView) container.getChildAt(toRealPosition);
                if (textView == null) {
                    textView = new TextView(getContext());
                    textView.setTextColor(Color.RED);
                    textView.setTextSize(18);
                    textView.setGravity(Gravity.CENTER);
                    container.addView(textView);
                }
                textView.setText("position=" + toRealPosition);
                return textView;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position % (4 + 2);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int current = mViewPager.getCurrentItem();
                    int last = mViewPager.getAdapter().getCount() - 2;
                    if (current == 0) {
                        mViewPager.setCurrentItem(last, false);
                    } else if (current == last + 1) {
                        mViewPager.setCurrentItem(1, false);
                    }
                }
            }
        });
        mViewPager.setCurrentItem(1, false);
        swipeRefreshListView.getScrollView().addHeaderView(mViewPager);
        mAutoPlayHandler.sendEmptyMessageDelayed(1000, 3000);
    }

    private int toRealPosition(int position) {
        int realPosition = (position - 1) % 4;
        if (realPosition < 0)
            realPosition += 4;

        return realPosition;
    }

    @Override
    public void onDestroyView() {
        if (mAutoPlayHandler != null)
            mAutoPlayHandler.removeMessages(1000);
        super.onDestroyView();
    }


    @Override
    public void onRefresh() {
        index = 0;
        swipeRefreshListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                objects.add(0, "下拉 = " + (--index));
                adapter.notifyDataSetChanged();
                swipeRefreshListView.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onListLoad() {
        ++index;
        swipeRefreshListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                int count = pagerSize + 10;
                for (int i = pagerSize; i < count; i++) {
                    objects.add("上拉 = " + i);
                }
                pagerSize = count;
                adapter.notifyDataSetChanged();
                swipeRefreshListView.setLoading(false);

                if (index == 1) {
                    swipeRefreshListView.setLoadCompleted(true);
                }
            }
        }, 1000);
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
        //有head，position-1才是真实item
        Toast.makeText(getContext(), "" + (position - 1), Toast.LENGTH_SHORT).show();
    }
}
