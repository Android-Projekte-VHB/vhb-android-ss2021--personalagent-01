package com.ristudios.personalagent.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.ristudios.personalagent.R;

/**
 * Handles the creation of the Burgermenu. Every other activity should inherit from BaseActivity.
 */
public class BaseActivity extends AppCompatActivity implements DrawerLayout.DrawerListener, NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout mDrawerLayout;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected Toolbar toolbar;
    protected NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBurgerMenu();
    }

    protected void initBurgerMenu() {
        initToolbar();
        initDrawer();
        initToggle();
    }

    /**
     * Initializes the drawer-layout and navigation.
     */
    protected void initDrawer() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout.addDrawerListener(this);
    }

    /**
     * Initializes the custom toolbar.
     */
    protected void initToolbar() {
        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
    }

    /**
     * Initializes and connects the burger-button with drawer and toolbar.
     */
    protected void initToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.syncState();
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        invalidateOptionsMenu();
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        invalidateOptionsMenu();
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String classname = this.getClass().getSimpleName(); //MainActivity
        switch (item.getItemId()) {

            case R.id.nav_Start: {

                launchMainActivity(classname);
                break;
            }

            case R.id.nav_calender: {
                launchCalendarActivity(classname);
                break;
            }

            case R.id.nav_settings: {
                launchSettingsActivity(classname);
                break;
            }

            case R.id.nav_weekly: {
                launchWeeklyoverviewActivity(classname);
                break;
            }

            default:
                return false;

        }
        return false;

    }

    //region Launcher

    /**
     * Launches the {@link WeeklyOverviewActivity} if the params are correct.
     *
     * @param classname The name of the class currently active.
     */
    private void launchWeeklyoverviewActivity(String classname) {

        if (!classname.equals("WeeklyOverviewActivity")) {
            Intent intent = new Intent(this, WeeklyOverviewActivity.class);
            startActivity(intent);
        }
        if (!classname.equals("MainActivity") && !classname.equals("WeeklyOverviewActivity")) {
            finish();
        }
    }

    /**
     * Launches the {@link SettingsActivity} if the params are correct.
     *
     * @param classname The name of the class currently active.
     */
    private void launchSettingsActivity(String classname) {

        if (!classname.equals("SettingsActivity")) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        if (!classname.equals("MainActivity") && !classname.equals("SettingsActivity")) {
            finish();
        }
    }

    /**
     * Launches the {@link CalendarActivity} if the params are correct.
     *
     * @param classname The name of the class currently active.
     */
    private void launchCalendarActivity(String classname) {

        if (!classname.equals("CalendarActivity")) {
            Intent intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);
        }
        if (!classname.equals("MainActivity") && !classname.equals("CalendarActivity")) {
            finish();
        }
    }

    /**
     * Launches the {@link MainActivity} if the params are correct.
     *
     * @param classname The name of the class currently active.
     */
    private void launchMainActivity(String classname) {

        if (!classname.equals("MainActivity")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //endregion

}