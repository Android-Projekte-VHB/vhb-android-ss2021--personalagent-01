package com.ristudios.personalagent.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.model.ScrollMode;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;
import com.kizitonwose.calendarview.ui.ViewContainer;
import com.ristudios.personalagent.R;
import com.ristudios.personalagent.data.EntryManager;
import com.ristudios.personalagent.ui.adapter.DayViewContainer;
import com.ristudios.personalagent.ui.adapter.MonthViewContainer;
import com.ristudios.personalagent.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 * Responsible for displaying a calendar.
 */
public class CalendarActivity extends BaseActivity implements DayViewContainer.CalendarClickListener {

    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_calender);
        super.onCreate(savedInstanceState);
        initBurgerMenu();
        getSupportActionBar().setTitle(getResources().getString(R.string.calendar));
        setUpOnActivityResult();
        setUpCalendar();

    }

    private void setUpOnActivityResult() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            int y = data.getIntExtra("resultYear", 0);
                            int m = data.getIntExtra("resultMonth", 0);
                            YearMonth last = YearMonth.of(y, m);
                            CalendarView calendarView = findViewById(R.id.calendar_view);
                            calendarView.scrollToMonth(last);
                        }
                    }
                });
    }


    private void setUpCalendar() {
        CalendarView calendarView = findViewById(R.id.calendar_view);
        calendarView.setDayBinder(new DayBinder<DayViewContainer>() {

            @Override
            public @NotNull DayViewContainer create(@NotNull View view) {
                return new DayViewContainer(view, CalendarActivity.this);
            }

            @Override
            public void bind(@NotNull DayViewContainer dayViewContainer, @NotNull CalendarDay calendarDay) {
                dayViewContainer.day = calendarDay;
                dayViewContainer.calendarDayText.setText(String.valueOf(calendarDay.getDate().getDayOfMonth()));
                if (calendarDay.getOwner() != DayOwner.THIS_MONTH){
                    dayViewContainer.calendarDayText.setText("");
                }

            }


        });
        calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthViewContainer>() {
            @Override
            public @NotNull MonthViewContainer create(@NotNull View view) {
                return new MonthViewContainer(view);
            }

            @Override
            public void bind(@NotNull MonthViewContainer viewContainer, @NotNull CalendarMonth calendarMonth) {
                viewContainer.textView.setText(calendarMonth.getYearMonth().getMonth().name()+ " " + calendarMonth.getYear());
                viewContainer.txtMon.setText("Mon");
                viewContainer.txtTue.setText("Tue");
                viewContainer.txtWed.setText("Wed");
                viewContainer.txtThu.setText("Thu");
                viewContainer.txtFri.setText("Fri");
                viewContainer.txtSat.setText("Sat");
                viewContainer.txtSat.setTextColor(Color.RED);
                viewContainer.txtSun.setText("Sun");
                viewContainer.txtSun.setTextColor(Color.RED);
            }
        });
        calendarView.setScrollMode(ScrollMode.PAGED);
        YearMonth currentMonth = YearMonth.now();
        YearMonth firstMonth = currentMonth.minusMonths(120);
        YearMonth lastMonth = currentMonth.plusMonths(120);
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek);
        calendarView.scrollToMonth(currentMonth);
    }

    @Override
    public void onDateClicked(CalendarDay day) {
        startDetailActivity(day.getDate().getYear(), day.getDate().getMonthValue(), day.getDate().getDayOfMonth());
    }

    private void startDetailActivity(int year, int monthValue, int dayOfMonth) {
        Intent intent = new Intent(this, CalendarDayDetailActivity.class);
        intent.putExtra("year", year);
        intent.putExtra("month", monthValue);
        intent.putExtra("day", dayOfMonth);
        launcher.launch(intent);
    }


}