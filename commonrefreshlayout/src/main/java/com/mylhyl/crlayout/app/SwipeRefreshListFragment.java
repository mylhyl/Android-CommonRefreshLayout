package com.mylhyl.crlayout.app;

import com.mylhyl.crlayout.SwipeRefreshListView;

public abstract class SwipeRefreshListFragment extends SwipeRefreshAbsListFragment<SwipeRefreshListView> {

    @Override
    public SwipeRefreshListView createSwipeRefreshLayout() {
        return new SwipeRefreshListView(getActivity());
    }
}
