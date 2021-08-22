package com.ristudios.personalagent.data;

import android.content.Context;

import com.ristudios.personalagent.R;

public enum Category {
    WORK (R.string.work, R.color.work_color),
    FITNESS (R.string.fitness, R.color.fitness_color),
    HOBBY (R.string.hobby, R.color.hobby_color),
    APPOINTMENT (R.string.appointment, R.color.appointment_color);

    /**
     * tasks are divided into four categories (work, fitness, hobby, appointment)
     * and are colored accordingly to achieve an easy distinction in the UI
     */

    public final int name;
    public final int color;

    Category(int name, int color){
        this.name = name;
        this.color = color;
    }
}
