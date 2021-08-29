package com.ristudios.personalagent.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ristudios.personalagent.data.Entry;

@Database(entities = {Entry.class}, version = 2)
@TypeConverters({EntryAttributeTypeConverter.class})

public abstract class EntryDatabase extends RoomDatabase {

    public abstract EntryDAO entryDAO();

}
