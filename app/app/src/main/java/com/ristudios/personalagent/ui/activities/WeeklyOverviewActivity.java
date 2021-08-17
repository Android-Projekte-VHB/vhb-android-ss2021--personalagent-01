package com.ristudios.personalagent.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ristudios.personalagent.R;

public class WeeklyOverviewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_weekly_overview);
        super.onCreate(savedInstanceState);
        initBurger();
        getSupportActionBar().setTitle("Wochenübersicht");
    }
}