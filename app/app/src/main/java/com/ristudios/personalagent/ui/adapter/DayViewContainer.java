package com.ristudios.personalagent.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.ViewContainer;
import com.ristudios.personalagent.R;

import org.jetbrains.annotations.NotNull;

import java.sql.ClientInfoStatus;

public class DayViewContainer extends ViewContainer {

    public final TextView calendarDayText;
    public CalendarDay day;

    public DayViewContainer(@NotNull View view, CalendarClickListener listener) {
        super(view);
        calendarDayText = view.findViewById(R.id.txt_calendar_day_text);
        calendarDayText.setOnClickListener(v -> {
            if (day.getOwner() == DayOwner.THIS_MONTH){
                listener.onDateClicked(day);
            }

        });
    }



    public interface CalendarClickListener{
        void onDateClicked(CalendarDay day);
    }

}
