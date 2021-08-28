package com.ristudios.personalagent.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;

public class EntryManager {

    private Context context;
    private ArrayList<Entry> entries;
    private EntryManagerListener listener;

    public EntryManager(Context context, EntryManagerListener listener){
        this.context = context;
        this.entries = new ArrayList<>();
        this.listener = listener;
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
        listener.onEntryListUpdated();
    }

    public void deleteEntry(Entry entry) {
        for (Entry currentEntry: entries) {
            if(entry.getId().equals(currentEntry.getId())) {
                entries.remove(entry);
            }
        }
    }

    public void removeEntry(Entry entry){
        Iterator<Entry> iter = entries.iterator();
        while (iter.hasNext()) {
            Entry str = iter.next();
            if (str.getId().equals(entry.getId()))
                iter.remove();
        }
    }

    public ArrayList<Entry> getCurrentEntries() {
        return new ArrayList<>(entries);
    }

    public void requestUpdate() {
        listener.onEntryListUpdated();
    }

    public interface EntryManagerListener{
        void onEntryListUpdated();
    }
}
