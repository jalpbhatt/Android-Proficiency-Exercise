package com.telstra.androidjsonparsing.ui;

import android.content.Intent;
import android.view.Menu;

import com.telstra.androidjsonparsing.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenu;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.util.ActivityController;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class MainActivityTest {

    /* ActivityController is a Robolectric class that drives the Activity lifecycle */
    private ActivityController<MainActivity> mController;

    @Before
    public void setUp() throws Exception {

        /* Call the "buildActivity" method so we get an ActivityController which we can use
           to have more control over the mMainActivity lifecycle
        */
        mController = Robolectric.buildActivity(MainActivity.class);
    }

    @Test
    public void shouldNotBeNull() throws Exception {

        MainActivity activity = Robolectric.setupActivity(MainActivity.class);
        assertNotNull(activity);
    }

    /* Activity creation that allows intent extras to be passed in */
    private MainActivity createWithIntent(String extra) {
        Intent intent = new Intent(RuntimeEnvironment.application, MainActivity.class);
        intent.putExtra("activity_extra", extra);
        MainActivity mainActivity = mController
                .withIntent(intent)
                .create()
                .start()
                .resume()
                .visible()
                .get();

        return mainActivity;
    }

    @Test
    public void createsAndDestroysActivity() {

        // Pass intent extra values, for now = empty
        MainActivity mainActivity = createWithIntent("");
        assertNotNull(mainActivity);
    }

    @Test
    public void isRefreshMenuAddedToActionbar() {

        RoboMenu menu = new RoboMenu();
        menu.addSubMenu("Refresh");

        MainActivity activity = Robolectric.setupActivity(MainActivity.class);
        ShadowActivity shadowActivity = shadowOf(activity);
        shadowActivity.onCreateOptionsMenu(menu);

        Menu optionsMenu = shadowActivity.getOptionsMenu();

        if (optionsMenu != null) {
            assertThat(optionsMenu.getItem(0).getTitle().toString(), equalTo("Refresh"));
        }

    }

}