package com.ristudios.personalagent.data;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;

import com.ristudios.personalagent.data.db.DatabaseOperationExecutor;
import com.ristudios.personalagent.data.db.EntryDatabaseHelper;
import com.ristudios.personalagent.utils.Utils;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntryManager {

    private Context context;
    private ArrayList<Entry> entries;
    private EntryManagerListener listener;
    private EntryDatabaseHelper db;
    private DatabaseOperationExecutor executor;

    public EntryManager(Context context, EntryManagerListener listener){
        this.context = context;
        this.listener = listener;
        this.entries = new ArrayList<>();
        db = new EntryDatabaseHelper(context);
        this.executor = new DatabaseOperationExecutor(db);
    }

    public void loadEntriesForToday() {
        entries.clear();
        long [] searchMillis = Utils.getSearchTimesForToday();
        executor.executeLoadForDateOperation(searchMillis[0], searchMillis[1], new DatabaseOperationExecutor.DataLoadedListener() {
            @Override
            public void onDataLoaded(List<Entry> loadedEntries) {
                entries.addAll(loadedEntries);
                listener.onListLoaded();
            }
        });
    }

    public void loadEntriesForDate(int year, int month, int day){
        entries.clear();
        long [] searchMillis = Utils.getSearchTimesForDate(year, month, day);
        Log.d(Utils.LOG_ALARM, "searchtimes: " + Utils.getDateFromMillis(searchMillis[0]) + " and " + Utils.getDateFromMillis(searchMillis[1]));
        executor.executeLoadForDateOperation(searchMillis[0], searchMillis[1], new DatabaseOperationExecutor.DataLoadedListener() {
            @Override
            public void onDataLoaded(List<Entry> loadedEntries) {
                entries.addAll(loadedEntries);
                listener.onListLoaded();

            }
        });
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
        executor.executeAddOrUpdateOperation(entry);
        listener.onEntryListUpdated();
    }

    public void removeEntry(Entry entry){
        Iterator<Entry> iter = entries.iterator();
        while (iter.hasNext()) {
            Entry str = iter.next();
            if (str.getId().equals(entry.getId()))
                iter.remove();
                executor.executeDeleteOperation(entry);
        }
    }

    public void addEntryAtPosition(int position, Entry entry) {
        entries.add(position, entry);
        executor.executeAddOrUpdateOperation(entry);
        listener.onEntryListUpdated();
    }

    public int getCurrentPoints()
    {
        int points = 0;
        for (Entry entry: entries){
            points = points + entry.getDifficulty().points;
        }
        return points;
    }

    public ArrayList<Entry> getCurrentEntries() {
        return new ArrayList<>(entries);
    }

    
    public void requestUpdate() {
        listener.onEntryListUpdated();
    }

    public interface EntryManagerListener{
        void onEntryListUpdated();
        void onListLoaded();

    }
}
