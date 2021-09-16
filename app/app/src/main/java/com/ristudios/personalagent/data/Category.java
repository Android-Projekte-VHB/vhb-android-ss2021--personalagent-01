package com.ristudios.personalagent.data;

import android.content.Context;

import com.ristudios.personalagent.R;

public enum Category {
    WORK (R.string.work),
    HOBBY (R.string.hobby),
    FITNESS (R.string.fitness),
    APPOINTMENT (R.string.appointment);



    public final int name;

    /**
     * tasks are divided into four categories (work, fitness, hobby, appointment)
     * and are colored accordingly to achieve an easy distinction in the UI
     */
    Category(int name){
        this.name = name;
    }
}
