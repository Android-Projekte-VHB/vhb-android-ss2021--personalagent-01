package com.ristudios.personalagent.ui.activities;

import android.os.Bundle;

import com.ristudios.personalagent.R;

/**
 * Responsible for displaying a weekly overview.
 */
public class WeeklyOverviewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_weekly_overview);
        super.onCreate(savedInstanceState);
        initBurgerMenu();
        getSupportActionBar().setTitle(getResources().getString(R.string.weekly_overview));




    }
}