package com.ristudios.personalagent.data.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ristudios.personalagent.data.Entry;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * operations and queries for database
 */
@Dao
public interface EntryDAO {

    @Insert
    void insertEntry(Entry entry);

    @Update
    void updateEntry(Entry entry);

    @Delete
    void deleteEntry(Entry entry);

    @Query("SELECT * from entries WHERE uuid= :id")
    Entry getEntryForId(UUID id);

    @Query("SELECT * from entries WHERE date>= :start AND date<= :end ")
    List<Entry> getEntriesForDate(long start, long end);

    @Query("SELECT * FROM entries")
    List<Entry> getAllEntries();
}
