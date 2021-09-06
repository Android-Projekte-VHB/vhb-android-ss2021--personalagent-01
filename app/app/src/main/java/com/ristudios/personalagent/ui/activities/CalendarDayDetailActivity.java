package com.ristudios.personalagent.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ristudios.personalagent.R;
import com.ristudios.personalagent.data.Category;
import com.ristudios.personalagent.data.Difficulty;
import com.ristudios.personalagent.data.Entry;
import com.ristudios.personalagent.data.EntryManager;
import com.ristudios.personalagent.ui.adapter.EntryAdapter;
import com.ristudios.personalagent.ui.fragments.AddOrUpdateEntryDialogFragment;
import com.ristudios.personalagent.utils.Utils;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class CalendarDayDetailActivity extends AppCompatActivity implements EntryManager.EntryManagerListener, EntryAdapter.OnEntryEditedListener, AddOrUpdateEntryDialogFragment.AddEntryDialogClickListener {

    private ZonedDateTime zonedDateTime;
    private EntryManager entryManager;
    private EntryAdapter entryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_day_detail);
        initData();
        initUI();

    }

    /**
     * Initiates the UI of the Activity.
     */
    private void initUI() {
        TextView txtHeader = findViewById(R.id.txt_detail_header);
        txtHeader.setText(Utils.getLocalizedFormattedDate(zonedDateTime));
        ImageButton btnPreviousDate = findViewById(R.id.btn_previoius_date);
        ImageButton btnNextDate = findViewById(R.id.btn_next_date);
        btnPreviousDate.setOnClickListener(v -> {
            zonedDateTime = zonedDateTime.minusDays(1);
            updateData();
        });
        btnNextDate.setOnClickListener(v -> {
            zonedDateTime = zonedDateTime.plusDays(1);
            updateData();
        });
        Button btnAddEntry = findViewById(R.id.btn_add_entry_calendar);
        btnAddEntry.setOnClickListener(v -> {
            AddOrUpdateEntryDialogFragment dialog = new AddOrUpdateEntryDialogFragment();
            dialog.setTargetDateTime(zonedDateTime);
            dialog.setMode(AddOrUpdateEntryDialogFragment.MODE_NEW);
            dialog.show(getSupportFragmentManager(), "pa:AddDialogCalendar");
        });
    }

    /**
     * Initiates the data of the current activity.
     */
    private void initData() {
        entryManager = new EntryManager(this, this);
        entryAdapter = new EntryAdapter(this, this);
        zonedDateTime = ZonedDateTime.now().with(LocalDate.of(getIntent().getIntExtra("year", 0), getIntent().getIntExtra("month", 1), getIntent().getIntExtra("day", 1)));
        RecyclerView recyclerView = findViewById(R.id.recycler_calendar_day);
        recyclerView.setAdapter(entryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        entryManager.loadEntriesForDate(zonedDateTime.getYear(), zonedDateTime.getMonthValue(), zonedDateTime.getDayOfMonth());
    }

    /**
     * Updates the displayed data when the buttons for next or previous day are clicked.
     */
    private void updateData() {
        TextView txtHeader = findViewById(R.id.txt_detail_header);
        txtHeader.setText(Utils.getLocalizedFormattedDate(zonedDateTime));
        entryManager.loadEntriesForDate(zonedDateTime.getYear(), zonedDateTime.getMonthValue(), zonedDateTime.getDayOfMonth());
        //ArrayList<Entry> sortedList = Utils.sortListByCategory(entryManager.getCurrentEntries());

    }

    @Override
    public void onEntryListUpdated() {
        runOnUiThread(() -> {
            entryAdapter.updateEntries(entryManager.getCurrentEntries());
            entryAdapter.notifyDataSetChanged();
        });

    }

    @Override
    public void onListLoaded() {
        entryManager.requestUpdate();
        Log.d(Utils.LOG_ALARM, String.valueOf(entryManager.getCurrentEntries().size()));
    }

    @Override
    public void onEntryEdited(Entry entry, int position) {
        AddOrUpdateEntryDialogFragment dialog = new AddOrUpdateEntryDialogFragment();
        dialog.setMode(AddOrUpdateEntryDialogFragment.MODE_UPDATE);
        dialog.setEntry(entry, position);
        dialog.show(getSupportFragmentManager(), "pa:UpdateEntryDialog");
    }

    /**
     * Adds a new entry to the entryManager.
     * @param name Name of the entry.
     * @param hour Hour of day for the entry.
     * @param minute Minute of hour for the new entry.
     * @param category Category of the entry.
     * @param difficulty difficulty of the entry.
     * @param targetDate The date of the entry.
     */
    @Override
    public void onItemNew(String name, int hour, int minute, Category category, Difficulty difficulty, ZonedDateTime targetDate) {
        Entry entry = new Entry(name, category, difficulty, Utils.millisForEntryWithDate(targetDate.getYear(), targetDate.getMonthValue(),
                targetDate.getDayOfMonth(), hour, minute));
        entryManager.addEntry(entry);
    }

    /**
     * Updates an entry by removing the old one and then adding a new one to entrymanager.
     * @param name Name of the entry.
     * @param hour Hour of day for the entry.
     * @param minute Minute of hour for the entry.
     * @param category Category of the entry.
     * @param difficulty Difficulty of the entry.
     * @param oldEntry The entry to be removed.
     * @param position Position of the edited entry.
     * @param targetDate The date of the entry.
     */
    @Override
    public void onItemUpdate(String name, int hour, int minute, Category category, Difficulty difficulty, Entry oldEntry, int position, ZonedDateTime targetDate) {
        entryManager.removeEntry(oldEntry);
        Entry entry = new Entry(name, category, difficulty, Utils.millisForEntryWithDate(targetDate.getYear(),
                targetDate.getMonthValue(), targetDate.getDayOfMonth(),
                hour, minute));
        entryManager.addEntryAtPosition(position, entry);
        entryAdapter.notifyDataSetChanged();
        Toast.makeText(this, getString(R.string.toast_changes_successful), Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays a Toast when the user clicks the negative option of the {@link AddOrUpdateEntryDialogFragment}.
     * @param mode Dialog mode (add or update).
     */
    @Override
    public void onNegativeClicked(int mode) {
        if (mode == 1) {
            Toast.makeText(this, getString(R.string.toast_new_entry_discarded), Toast.LENGTH_SHORT).show();
        }
        else if (mode == -1){
            Toast.makeText(this, getString(R.string.toast_changes_discarded), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("resultYear", zonedDateTime.getYear());
        intent.putExtra("resultMonth", zonedDateTime.getMonthValue());
        setResult(RESULT_OK, intent);
        finish();
    }
}