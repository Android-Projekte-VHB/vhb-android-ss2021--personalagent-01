package com.ristudios.personalagent.data;

import java.util.Date;
import java.util.UUID;

public class Entry {

    //Represents an Entry for the To-Do-List

    private final String uuid;
    private final String name;
    private final Category category;
    private final Difficulty difficulty;
    private final long date;


    public Entry(String name, Category category, Difficulty difficulty, long date){
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public long getDate() {
        return date;
    }

    public String getId() {
        return uuid;
    }

    public Entry copy() {
        return new Entry(name, category, difficulty, date);
    }
}
