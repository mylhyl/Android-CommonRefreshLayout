package com.mylhyl.crlayout.sample.app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.litesuits.http.LiteHttp;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.mylhyl.crlayout.internal.LoadConfig;
import com.mylhyl.crlayout.app.SwipeRefreshListFragment;
import com.mylhyl.crlayout.sample.R;
import com.mylhyl.cygadapter.CygAdapter;
import com.mylhyl.cygadapter.CygViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hupei on 2016/5/13.
 */
public class AppListFragment extends SwipeRefreshListFragment {
    private static final String URL = "http://image.baidu.com/";
    private static final String REGEX_IMG = "<img.*?src=\"http://(.*?).jpg\"";
    private CygAdapter<String> adapter;
    private List<String> objects = new ArrayList<>();
    private LiteHttp liteHttp;
    private int index;

    public static AppListFragment newInstance() {
        return new AppListFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        getSwipeRefreshLayout().setLoadAnimator(true);
        setEnabledLoad(true);

        adapter = new CygAdapter<String>(getContext(), R.layout.image_item, objects) {
            @Override
            public void onBindData(CygViewHolder viewHolder, String item, int position) {
                ImageView imageView = viewHolder.findViewById(R.id.img_item);
                Glide.with(mContext)
                        .load(item.toString())
                        .into(imageView);
            }
        };
        setListAdapter(adapter);
        setEmptyText("无数据");
        LoadConfig loadConfig = getSwipeRefreshLayout().getLoadConfig();
        loadConfig.setLoadCompletedText("亲，数据加载完了！");
        if (liteHttp == null) {
            liteHttp = LiteHttp.build(getContext())
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000)
                    .create();
        } else {
            liteHttp.getConfig()
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000);
        }
        autoRefresh(R.color.colorPrimary);
    }

    @Override
    public void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onListItemClick(parent, view, position, id);
        Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        index = 0;
        adapter.clear(false);
        executeAsync(URL);
        getSwipeRefreshLayout().postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onListLoad() {
        executeAsync(URL);
        getSwipeRefreshLayout().postDelayed(new Runnable() {
            @Override
            public void run() {
                setLoading(false);
                ++index;
                if (index == 1) {
                    getSwipeRefreshLayout().setLoadCompleted(true);
                }
            }
        }, 1000);
    }

    private void executeAsync(String url) {
        StringRequest stringRequest = new StringRequest(url).setMethod(HttpMethods.Get)
                .setHttpListener(new HttpListener<String>() {
                    @Override
                    public void onLoading(AbstractRequest<String> request, long total, long len) {
                        super.onLoading(request, total, len);
                    }

                    @Override
                    public void onSuccess(String s, Response<String> response) {
                        String result = response.getResult();
                        List<String> imgSrcList = getImgSrcList(result);
                        adapter.addAll(imgSrcList);
                    }

                    @Override
                    public void onStart(AbstractRequest<String> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onUploading(AbstractRequest<String> request, long total, long len) {
                        super.onUploading(request, total, len);
                    }
                });
        liteHttp.executeAsync(stringRequest);
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

    private static List<String> getImgSrcList(String htmlStr) {
        List<String> pics = new ArrayList<>();
        Pattern pImage = Pattern.compile(REGEX_IMG, Pattern.CASE_INSENSITIVE);
        Matcher mImage = pImage.matcher(htmlStr);
        while (mImage.find()) {
            String src = mImage.group(1);
            if (src.length() < 100) {
                pics.add("http://" + src + ".jpg");
            }
        }
        return pics;
    }
}
