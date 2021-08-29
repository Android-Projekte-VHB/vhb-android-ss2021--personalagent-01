package com.ristudios.personalagent.data.db;

import android.content.Context;

import androidx.room.Room;

import com.ristudios.personalagent.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class EntryDatabaseHelper {

    private static final String DATABASE_NAME = "personalAgent-db";
    private EntryDatabase db;
    private final Context context;

    public EntryDatabaseHelper(Context context) {
        this.context = context;
        initDatabase(context);
    }

    private void initDatabase(Context context) {
        //@TODO: no main thread queries!!!
        db = Room.databaseBuilder(context, EntryDatabase.class, DATABASE_NAME).build();
    }

    public void addOrUpdateEntry(Entry entry) {
        Entry entryFromDatabase = db.entryDAO().getEntryForId(entry.getId());
        if (entryFromDatabase == null) {
            db.entryDAO().insertEntry(entry);
        } else {
            db.entryDAO().updateEntry(entry);
        }
    }

    public void deleteEntry(Entry entry) {
        db.entryDAO().deleteEntry(entry);
    }

    public ArrayList<Entry> getEntriesForDate(long start, long end) {
        return new ArrayList<Entry>(db.entryDAO().getEntriesForDate(start, end));
    }

    public ArrayList<Entry> getAllEntries() {
        return new ArrayList<Entry>(db.entryDAO().getAllEntries());
    }

}

