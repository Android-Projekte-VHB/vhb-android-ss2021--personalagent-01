package com.ristudios.personalagent.data;

import android.content.Context;
import android.provider.ContactsContract;

import com.ristudios.personalagent.data.db.DatabaseOperationExecutor;
import com.ristudios.personalagent.data.db.EntryDatabaseHelper;

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
    private ZonedDateTime zonedDateTime;
    private DatabaseOperationExecutor executor;

    public EntryManager(Context context, EntryManagerListener listener){
        this.context = context;
        this.listener = listener;
        this.entries = new ArrayList<>();
        db = new EntryDatabaseHelper(context);
        this.executor = new DatabaseOperationExecutor(db);
    }

    public void loadEntriesForToday() {
        zonedDateTime = ZonedDateTime.now(ZoneId.systemDefault());
        LocalTime start = LocalTime.of(0,0,0);
        LocalTime end = LocalTime.of(23, 59, 59);
        long startMillis = zonedDateTime.with(start).toInstant().toEpochMilli();
        long endMillis = zonedDateTime.with(end).toInstant().toEpochMilli();
        executor.executeLoadForDateOperation(startMillis, endMillis, new DatabaseOperationExecutor.DataLoadedListener() {
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
