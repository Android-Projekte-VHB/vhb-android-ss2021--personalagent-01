package com.ristudios.personalagent.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ristudios.personalagent.data.Entry;

@Database(entities = {Entry.class}, version = 3)
@TypeConverters({EntryAttributeTypeConverter.class})

public abstract class EntryDatabase extends RoomDatabase {
    //holds instance of EntryDAO class for DataBaseHelper class
    public abstract EntryDAO entryDAO();

}
