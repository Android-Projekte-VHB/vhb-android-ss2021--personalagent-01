package com.ristudios.personalagent.data;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class EntryManager {

    private final ArrayList<Entry> entries;

    public EntryManager() {
        entries = new ArrayList<>();
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    public void getEntryForDate() {
        //TODO: return Entry, param: date
    }

    public ArrayList<Entry> getCopyOfCurrentList() {
        ArrayList<Entry> currentList = new ArrayList<>();
        currentList.addAll(entries); //oder for each?
        return currentList;
    }

    public void removeEntry(Entry entry) {
        //TODO: UUID für entries als identifier zum Löschen
    }

}
