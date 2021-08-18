package com.ristudios.personalagent.data;

import java.util.Date;
import java.util.UUID;

public class Entry {

    //Represents an Entry for the To-Do-List

    private final String uuid;
    private final String name;
    private final String category; //Will be Enum
    private final String difficulty; //Will be Enum
    private final long date;


    /*@TODO Mary: Turn both category and points/difficulty into enums
        Category Enum: should contain the 3 different categories 'work', 'hobby' and 'appointment'
        Difficulty Enum: should contain the 3 different levels of difficulty 'easy', 'medium' and 'hard' as an identifier and the rewarded points for completion (10,20,30?)
    */

    public Entry(String name, String category, String difficulty, long date){
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public long getDate() {
        return date;
    }
}
