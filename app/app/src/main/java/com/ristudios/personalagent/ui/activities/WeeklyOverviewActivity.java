package com.ristudios.personalagent.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.preference.PreferenceManager;
import com.ristudios.personalagent.R;
import com.ristudios.personalagent.utils.Utils;

/**
 * Responsible for displaying a weekly overview.
 */
public class WeeklyOverviewActivity extends BaseActivity {

    private SharedPreferences prefs;
    private int dailyGoalInPointsValue, workPercentageValue, lifePercentageValue;
    private String dailyGoalInPoints;
    private ProgressBar workProgressBar, hobbyProgressBar, fitnessProgressBar;
    private TextView workProgressTV, hobbyProgressTV, fitnessProgressTV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_weekly_overview);
        super.onCreate(savedInstanceState);
        initBurgerMenu();
        getSupportActionBar().setTitle(getResources().getString(R.string.weekly_overview));
        initData();
        initProgressBars();
        setProgressOnBars();
        setProgressOnText();

    }

    private void initData(){
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        dailyGoalInPoints = prefs.getString(Utils.SP_DAILY_GOAL_KEY, "100");
        dailyGoalInPointsValue = Integer.parseInt(dailyGoalInPoints);
        //work-life balance 2:1:1
        workPercentageValue = (int) (dailyGoalInPointsValue * Utils.FACTOR_GOAL_PER_WEEK * Utils.FACTOR_WORK_BALANCE);
        lifePercentageValue = (int) (dailyGoalInPointsValue * Utils.FACTOR_GOAL_PER_WEEK * Utils.FACTOR_LIFE_BALANCE);
    }

    private void initProgressBars(){
        workProgressBar = findViewById(R.id.work_progress_bar);
        fitnessProgressBar = findViewById(R.id.fitness_progress_bar);
        hobbyProgressBar = findViewById(R.id.hobby_progress_bar);
        workProgressTV = findViewById(R.id.work_progress_text);
        hobbyProgressTV= findViewById(R.id.hobby_progress_text);
        fitnessProgressTV = findViewById(R.id.fitness_progress_text);
        workProgressBar.setMax(workPercentageValue);
        fitnessProgressBar.setMax(lifePercentageValue);
        hobbyProgressBar.setMax(lifePercentageValue);
    }

    private void setProgressOnBars(){
        workProgressBar.setProgress(prefs.getInt(Utils.SP_WORK_TOTAL_POINTS_KEY, 0));
        hobbyProgressBar.setProgress(prefs.getInt(Utils.SP_HOBBY_TOTAL_POINTS_KEY, 0));
        fitnessProgressBar.setProgress(prefs.getInt(Utils.SP_FITNESS_TOTAL_POINTS_KEY, 0));
    }

    private void setProgressOnText(){
        String workText = workProgressTV.getText().toString().replace("$CURRENT", String.valueOf(prefs.getInt(Utils.SP_WORK_TOTAL_POINTS_KEY, 0))).replace("$MAX", String.valueOf(workPercentageValue));
        workProgressTV.setText(workText);

        String hobbyText = hobbyProgressTV.getText().toString().replace("$CURRENT", String.valueOf(prefs.getInt(Utils.SP_HOBBY_TOTAL_POINTS_KEY, 0))).replace("$MAX", String.valueOf(lifePercentageValue));
        hobbyProgressTV.setText(hobbyText);

        String fitnessText = fitnessProgressTV.getText().toString().replace("$CURRENT", String.valueOf(prefs.getInt(Utils.SP_FITNESS_TOTAL_POINTS_KEY, 0))).replace("$MAX", String.valueOf(lifePercentageValue));
        fitnessProgressTV.setText(fitnessText);
    }

}