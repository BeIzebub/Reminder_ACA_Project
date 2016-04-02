package com.reminder;


import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.reminder.mobile_activities.AllCallRemindersActivity;
import com.reminder.mobile_activities.AllSMSRemindersActivity;
import com.reminder.other_activities.AllSimpleRemindersActivity;
import com.reminder.social_activities.AllFacebookReminders;
import com.reminder.social_activities.AllViberReminders;
import com.reminder.social_activities.MailActivity;
import com.reminder.social_activities.ViberActivity;


public class BaseActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout fullLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private int selectedNavItemId;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        /**
         * {@link FrameLayout} to inflate the child's view. We could also use a {@link android.view.ViewStub}
         */
        FrameLayout activityContainer = (FrameLayout) fullLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);


        super.setContentView(fullLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        if (useToolbar())
        {
            setSupportActionBar(toolbar);
        }
        else
        {
            toolbar.setVisibility(View.GONE);
        }

        setUpNavView();
    }

    protected boolean useToolbar()
    {
        return true;
    }

    protected void setUpNavView()
    {
        navigationView.setNavigationItemSelectedListener(this);

        if( useDrawerToggle()) {
            drawerToggle = new ActionBarDrawerToggle(this, fullLayout, toolbar,
                    R.string.nav_drawer_opened,
                    R.string.nav_drawer_closed);

            fullLayout.setDrawerListener(drawerToggle);
            drawerToggle.syncState();
        } else if(useToolbar() && getSupportActionBar() != null) {
            // Use home/back button instead
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(getResources()
                    .getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        }
    }


    protected boolean useDrawerToggle()
    {
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        fullLayout.closeDrawer(GravityCompat.START);
        selectedNavItemId = menuItem.getItemId();

        return onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.all_reminders:
                startActivity(new Intent(this, HomePageActivity.class));
                finish();
                return true;

            case R.id.mail:
                startActivity(new Intent(this, MailActivity.class));
                finish();
                return true;

            case R.id.reminders:
                startActivity(new Intent(this, AllSimpleRemindersActivity.class));
                finish();
                return true;

            case R.id.call:
                startActivity(new Intent(this, AllCallRemindersActivity.class));
                finish();
                return true;

            case R.id.sms :
                startActivity(new Intent(this, AllSMSRemindersActivity.class));
                finish();
                return true;

            case R.id.facebook:
                startActivity(new Intent(this, AllFacebookReminders.class));
                finish();
                return true;

            case R.id.viber:
                startActivity(new Intent(this, AllViberReminders.class));
                finish();
                return true;

            case R.id.settings :
                startActivity(new Intent(this, Settings.class));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}