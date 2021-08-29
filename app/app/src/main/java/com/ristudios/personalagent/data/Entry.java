package com.ristudios.personalagent.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.UUID;
@Entity(tableName = "entries")
public class Entry {

    //Represents an Entry for the To-Do-List

    @PrimaryKey
    @NonNull
    public String uuid;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public Category category;
    @ColumnInfo
    public Difficulty difficulty;
    @ColumnInfo
    public final long date;

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
