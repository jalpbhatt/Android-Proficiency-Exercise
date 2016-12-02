package com.telstra.androidjsonparsing.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.telstra.androidjsonparsing.AppConfig;
import com.telstra.androidjsonparsing.R;
import com.telstra.androidjsonparsing.utils.NetworkUtils;
import com.telstra.androidjsonparsing.utils.UiUtils;

public class MainActivity extends AppCompatActivity implements NewsFeedFragment.OnFragmentInteractionListener {

    /* UI variables */
    private android.support.v7.app.ActionBar mActionbar;

    /* Reference variables */
    private Fragment mNewsFeedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Verify storage permissions at run time
        AppConfig.verifyStoragePermissions(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionbar = getSupportActionBar();

        mNewsFeedFragment = NewsFeedFragment.newInstance();

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mNewsFeedFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        NewsFeedFragment fragment = ((NewsFeedFragment) mNewsFeedFragment);
        if (id == R.id.action_refresh && !fragment.mIsSwipeToRef) {
            if (NetworkUtils.isNetworkAvailable(getApplicationContext())) {
                fragment.hideConnectionLayout();
                fragment.refreshCountryDetails();
            } else {
                UiUtils.showToast(getApplicationContext(), R.string.error_msg_no_internet_connection);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setActionbarTitle(String title) {

        if (mActionbar != null) {
            mActionbar.setTitle(title);
        }
    }
}
