package com.ristudios.personalagent.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.UUID;

//Represents an Entry for the To-Do-List
@Entity(tableName = "entries")
public class Entry {

    @PrimaryKey
    @NonNull
    public final UUID uuid;
    public final String name;
    public final Category category;
    public final Difficulty difficulty;
    public final long date;

    @Ignore
    public Entry(String name, Category category, Difficulty difficulty, long date) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.date = date;
    }

    public Entry(String name, Category category, Difficulty difficulty, long date, @NotNull UUID uuid) {
        this.uuid = uuid;
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

    public UUID getId() {
        return uuid;
    }

    public Entry copy() {
        return new Entry(name, category, difficulty, date);
    }
}
