package com.ristudios.personalagent.ui.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.model.ScrollMode;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;
import com.kizitonwose.calendarview.ui.ViewContainer;
import com.ristudios.personalagent.R;
import com.ristudios.personalagent.ui.adapter.DayViewContainer;
import com.ristudios.personalagent.ui.adapter.MonthViewContainer;
import com.ristudios.personalagent.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.YearMonth;
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
        setUpCalendar(this);

    }

    /**
     * Registers this Activity to receive ActivityResults.
     */
    private void setUpOnActivityResult() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        int y = data.getIntExtra("resultYear", 0);
                        int m = data.getIntExtra("resultMonth", 0);
                        YearMonth last = YearMonth.of(y, m);
                        CalendarView calendarView = findViewById(R.id.calendar_view);
                        calendarView.scrollToMonth(last);
                    }
                });
    }


    /**
     * Sets up the calendar by creating and binding a {@link DayViewContainer} to display texts for single days and also a
     * {@link MonthViewContainer} to display a Header for the current selected month.
     *
     * @param context Application context.
     */
    private void setUpCalendar(Context context) {
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
                if (calendarDay.getOwner() != DayOwner.THIS_MONTH) {
                    dayViewContainer.calendarDayText.setText("");
                }

            }


        });
        String[] weekdays_short = context.getResources().getStringArray(R.array.weekdays_short);
        calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthViewContainer>() {
            @Override
            public @NotNull MonthViewContainer create(@NotNull View view) {
                return new MonthViewContainer(view);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void bind(@NotNull MonthViewContainer viewContainer, @NotNull CalendarMonth calendarMonth) {
                String localMonthName = Utils.getLocalMonthName(calendarMonth.getYearMonth().getMonth().name().toLowerCase(), context);
                viewContainer.txtHeader.setText(localMonthName + " " + calendarMonth.getYear());
                viewContainer.txtMon.setText(weekdays_short[0]);
                viewContainer.txtTue.setText(weekdays_short[1]);
                viewContainer.txtWed.setText(weekdays_short[2]);
                viewContainer.txtThu.setText(weekdays_short[3]);
                viewContainer.txtFri.setText(weekdays_short[4]);
                viewContainer.txtSat.setText(weekdays_short[5]);
                viewContainer.txtSat.setTextColor(Color.RED);
                viewContainer.txtSun.setText(weekdays_short[6]);
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

    /**
     * starts the {@link CalendarDayDetailActivity} class with a year, month, day which will then display
     * all entries for the selected date.
     *
     * @param year       The year of the date.
     * @param monthValue The month of the date.
     * @param dayOfMonth The day of month.
     */
    private void startDetailActivity(int year, int monthValue, int dayOfMonth) {
        Intent intent = new Intent(this, CalendarDayDetailActivity.class);
        intent.putExtra("year", year);
        intent.putExtra("month", monthValue);
        intent.putExtra("day", dayOfMonth);
        launcher.launch(intent);
    }


}