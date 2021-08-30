package com.ristudios.personalagent.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.kizitonwose.calendarview.ui.ViewContainer;
import com.ristudios.personalagent.R;

import org.jetbrains.annotations.NotNull;

public class MonthViewContainer extends ViewContainer {

    public final TextView textView;

    public MonthViewContainer(@NotNull View view) {
        super(view);
        textView = view.findViewById(R.id.exTwoHeaderText);
    }
}
