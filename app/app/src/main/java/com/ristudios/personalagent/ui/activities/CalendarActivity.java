package com.ristudios.personalagent.ui.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 * Responsible for displaying a calendar.
 */
public class CalendarActivity extends BaseActivity implements DayViewContainer.CalendarClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_calender);
        super.onCreate(savedInstanceState);
        initBurgerMenu();
        getSupportActionBar().setTitle(getResources().getString(R.string.calendar));
        setUpCalendar();
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
                if (calendarDay.getOwner() == DayOwner.THIS_MONTH){
                    dayViewContainer.calendarDayText.setTextColor(Color.BLACK);
                }
                else {
                    dayViewContainer.calendarDayText.setTextColor(Color.GRAY);
                }
                dayViewContainer.calendarDayText.setText(Utils.getFormattedDate(ZonedDateTime.now().with(calendarDay.getDate())));
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
            }
        });
        calendarView.setScrollMode(ScrollMode.PAGED);
        YearMonth currentMonth = YearMonth.now();
        YearMonth firstMonth = currentMonth.minusMonths(12);
        YearMonth lastMonth = currentMonth.plusMonths(48);
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek);
        calendarView.scrollToMonth(currentMonth);
    }

    @Override
    public void onDateClicked(CalendarDay day) {
        Toast.makeText(this, "You clicked on " + Utils.getFormattedDate(ZonedDateTime.now().with(day.getDate())), Toast.LENGTH_SHORT).show();
    }
}