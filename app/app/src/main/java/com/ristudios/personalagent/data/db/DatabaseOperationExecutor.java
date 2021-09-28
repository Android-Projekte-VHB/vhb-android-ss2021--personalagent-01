package com.ristudios.personalagent.data.db;

import com.ristudios.personalagent.data.Entry;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DatabaseOperationExecutor {

    private EntryDatabaseHelper helper;

    /**
     * this class executes database operations within an extra thread
     *
     * @param helper instance of DatabaseHelper which gives access to the database
     */


    public DatabaseOperationExecutor(EntryDatabaseHelper helper) {
        this.helper = helper;
    }

    public void executeAddOrUpdateOperation(Entry entry) {
        Executor e = Executors.newSingleThreadExecutor();
        e.execute(() -> helper.addOrUpdateEntry(entry));
    }

    public void executeDeleteOperation(Entry entry) {
        Executor e = Executors.newSingleThreadExecutor();
        e.execute(() -> helper.deleteEntry(entry));
    }

    public void executeLoadForDateOperation(long start, long end, DataLoadedListener listener) {
        Executor e = Executors.newSingleThreadExecutor();
        e.execute(() -> listener.onDataLoaded(helper.getEntriesForDate(start, end))
        );
    }

    /**
     * listener informs manager when data is loaded
     */
    public interface DataLoadedListener {
        void onDataLoaded(List<Entry> loadedEntries);
    }
}
