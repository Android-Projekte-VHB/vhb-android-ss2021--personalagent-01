package com.ristudios.personalagent.ui.activities;

import android.os.Bundle;

import com.ristudios.personalagent.R;
import com.ristudios.personalagent.ui.fragments.SettingsFragment;

/**
 * Responsible for handling the settings of the app.
 */
public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_settings);
        super.onCreate(savedInstanceState);
        initBurgerMenu();
        getSupportActionBar().setTitle(getResources().getString(R.string.settings));
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_view, new SettingsFragment()).commit();
    }
}