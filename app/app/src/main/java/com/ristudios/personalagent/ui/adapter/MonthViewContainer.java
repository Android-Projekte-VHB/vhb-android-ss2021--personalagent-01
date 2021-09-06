package com.ristudios.personalagent.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.kizitonwose.calendarview.ui.ViewContainer;
import com.ristudios.personalagent.R;

import org.jetbrains.annotations.NotNull;

public class MonthViewContainer extends ViewContainer {

    public final TextView txtHeader, txtMon, txtTue, txtWed, txtThu, txtFri, txtSat, txtSun;

    public MonthViewContainer(@NotNull View view) {
        super(view);
        txtHeader = view.findViewById(R.id.exTwoHeaderText);
        txtMon = view.findViewById(R.id.txt_mon);
        txtTue = view.findViewById(R.id.txt_tue);
        txtWed = view.findViewById(R.id.txt_wed);
        txtThu = view.findViewById(R.id.txt_thu);
        txtFri = view.findViewById(R.id.txt_fri);
        txtSat = view.findViewById(R.id.txt_sat);
        txtSun = view.findViewById(R.id.txt_sun);
    }
}
