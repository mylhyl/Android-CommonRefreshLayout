package com.mylhyl.crlayout.sample;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.mylhyl.crlayout.sample.app.AppExpandableListFragment;
import com.mylhyl.crlayout.sample.app.AppListFragment;
import com.mylhyl.crlayout.sample.app.GridViewFragment;
import com.mylhyl.crlayout.sample.app.ListViewFragment;
import com.mylhyl.crlayout.sample.app.RecyclerViewFragment;
import com.mylhyl.crlayout.sample.app.WebViewFragment;

public class MainActivity extends AppCompatActivity implements TypesFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, TypesFragment.newInstance())
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Fragment fragment = null;
        int typeId = (int) ContentUris.parseId(uri);
        switch (typeId) {
            case 0:
                fragment = ListViewFragment.newInstance();
                break;
            case 1:
                fragment = GridViewFragment.newInstance();
                break;
            case 2:
                fragment = RecyclerViewFragment.newInstance();
                break;
            case 3:
                fragment = WebViewFragment.newInstance();
                break;
            case 4:
                fragment = AppListFragment.newInstance();
                break;
            case 5:
                fragment = AppExpandableListFragment.newInstance();
                break;
        }
        if (fragment != null)
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
    }
}
