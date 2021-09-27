package com.ristudios.personalagent.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ristudios.personalagent.R;
import com.ristudios.personalagent.data.Category;
import com.ristudios.personalagent.data.Difficulty;
import com.ristudios.personalagent.data.Entry;
import com.ristudios.personalagent.data.EntryManager;
import com.ristudios.personalagent.ui.adapter.EntryAdapter;
import com.ristudios.personalagent.ui.fragments.AddOrUpdateEntryDialogFragment;
import com.ristudios.personalagent.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class CalendarDayDetailActivity extends AppCompatActivity implements EntryManager.EntryManagerListener, EntryAdapter.OnEntryEditedListener, AddOrUpdateEntryDialogFragment.AddEntryDialogClickListener {

    private ZonedDateTime zonedDateTime;
    private EntryManager entryManager;
    private EntryAdapter entryAdapter;
    private RecyclerView recyclerView;
    private Paint swipeColor = new Paint();
    private Bitmap iconDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_day_detail);
        initData();
        initUI();
        initAdapterGestures();

    }

    /**
     * Initiates the UI of the Activity.
     */
    private void initUI() {
        TextView txtHeader = findViewById(R.id.txt_detail_header);
        txtHeader.setText(Utils.getLocalizedFormattedDate(zonedDateTime));
        ImageButton btnPreviousDate = findViewById(R.id.btn_previoius_date);
        ImageButton btnNextDate = findViewById(R.id.btn_next_date);
        iconDelete = Utils.drawableToBitmap(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_baseline_delete_sweep_32));
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
        recyclerView = findViewById(R.id.recycler_calendar_day);
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
     *
     * @param name       Name of the entry.
     * @param hour       Hour of day for the entry.
     * @param minute     Minute of hour for the new entry.
     * @param category   Category of the entry.
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
     *
     * @param name       Name of the entry.
     * @param hour       Hour of day for the entry.
     * @param minute     Minute of hour for the entry.
     * @param category   Category of the entry.
     * @param difficulty Difficulty of the entry.
     * @param oldEntry   The entry to be removed.
     * @param position   Position of the edited entry.
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
     *
     * @param mode Dialog mode (add or update).
     */
    @Override
    public void onNegativeClicked(int mode) {
        if (mode == 1) {
            Toast.makeText(this, getString(R.string.toast_new_entry_discarded), Toast.LENGTH_SHORT).show();
        } else if (mode == -1) {
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

    private void initAdapterGestures() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            /**
             * Handles Drag-And-Drop Gestured. Not used.
             */
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            /**
             * Handles the Swipe-Gestures on Entries in the RecyclerView.
             * The SwitchCase determines what happens if the user swipes left or right.
             * Swiping from Left to Right will mark an Entry as completed and points will be awarded to the user.
             * Swiping from Right to Left will delete an Entry. No points will be rewarded.
             * After each successfully performed Gesture, a Snackbar will appear at the bottom of the screen to inform the user about his action. It holds an UNDO-Button for easy reversal of actions.
             */
            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Entry deletedEntry = entryManager.getCurrentEntries().get(position);
                String snackbarUndoString = getResources().getString(R.string.item_undo_snackbar_button);

                switch (direction) {
                    case ItemTouchHelper.LEFT: //ITEM DELETED
                        Entry finalDeletedEntry = deletedEntry;
                        String snackbarDeletedString = getResources().getString(R.string.item_deleted_snackbar).replace("$ITEM", entryManager.getCurrentEntries().get(viewHolder.getAdapterPosition()).getName());
                        Snackbar.make(recyclerView, snackbarDeletedString, Snackbar.LENGTH_LONG).setAction(snackbarUndoString, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                entryManager.addEntry(finalDeletedEntry);
                                updateAdapterWithAnimation(position);
                            }
                        }).show();
                        entryManager.removeEntry(entryManager.getCurrentEntries().remove(position));
                        updateAdapterWithAnimation(position);
                }
            }

            /**
             * Here, the animation of Swiping is creating.
             * Swiping from Left to Right (dX > 0), a green Bar will appear, indicating that this action will mark an entry as completed.
             * Swiping from Right to Left, a red Bar will appear, indicating that this action will delete an entry.
             * Fitting Drawables help to understand the actions.
             */
            @Override
            public void onChildDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    // Get RecyclerView item from the ViewHolder
                    int h = iconDelete.getHeight();
                    View itemView = viewHolder.itemView;
                    int itemViewHeight = itemView.getHeight();
                    int dif = itemViewHeight - h;
                    if (dX > 0) {
                        return;
                    } else {
                        /* Set your color for negative displacement */
                        swipeColor.setColor(itemView.getResources().getColor(R.color.hard, null));
                        // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                        c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(),
                                (float) itemView.getRight(), (float) itemView.getBottom(), swipeColor);
                        c.drawBitmap(iconDelete, (float) itemView.getLeft() + itemView.getWidth() - iconDelete.getWidth() - (iconDelete.getWidth() / 10), (float) itemView.getTop() + dif / 2, swipeColor);

                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void updateAdapterWithAnimation(int position) {
        entryAdapter.notifyItemRemoved(position);
        entryAdapter.notifyItemRangeChanged(position, entryManager.getCurrentEntries().size());
        entryAdapter.updateEntries(entryManager.getCurrentEntries());
    }

}