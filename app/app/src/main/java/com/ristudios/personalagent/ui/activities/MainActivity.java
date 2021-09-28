package com.ristudios.personalagent.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import com.ristudios.personalagent.R;
import com.ristudios.personalagent.data.Category;
import com.ristudios.personalagent.data.Difficulty;
import com.ristudios.personalagent.data.Entry;
import com.ristudios.personalagent.data.EntryManager;
import com.ristudios.personalagent.data.api.Weather;
import com.ristudios.personalagent.data.api.WeatherDataListener;
import com.ristudios.personalagent.data.api.WeatherDataProvider;
import com.ristudios.personalagent.ui.adapter.EntryAdapter;
import com.ristudios.personalagent.ui.fragments.AddOrUpdateEntryDialogFragment;
import com.ristudios.personalagent.utils.Utils;
import com.ristudios.personalagent.utils.notifications.Alarm;
import com.ristudios.personalagent.utils.notifications.NotificationHelper;


import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;


/**
 * LauncherActivity, shows tasks for current day and weather information.
 */


public class MainActivity extends BaseActivity implements WeatherDataListener, EntryManager.EntryManagerListener, AddOrUpdateEntryDialogFragment.AddEntryDialogClickListener, EntryAdapter.OnEntryEditedListener {


    //For API-testing
    private WeatherDataProvider provider;
    private TextView tempTV, tempMaxTV, tempMinTV, precipitationTV, greetingsTV, dateTV;
    private ImageView weatherIcon;
    private RecyclerView recyclerView;
    private EntryAdapter entryAdapter;
    private EntryManager manager;
    private SharedPreferences prefs;
    private Paint swipeColor = new Paint();
    private Bitmap iconCheck, iconDelete;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        setupNotificationChannel();
        initBurgerMenu();
        initUI();
        initData();
        initAPI();
        getSupportActionBar().setTitle(getResources().getString(R.string.start));
        initializeAlarms();
    }


    @Override
    protected void onResume() {
        super.onResume();
        greetingsTV.setText(R.string.greetings_flavor);
        String userName = prefs.getString(Utils.SP_USERNAME_KEY, "");
        greetingsTV.setText(greetingsTV.getText().toString().replace("$NAME", userName));
        manager.loadEntriesForToday();
    }

    private void initData() {
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs.edit();
        manager = new EntryManager(this, this);
        entryAdapter = new EntryAdapter(this, this);
        recyclerView.setAdapter(entryAdapter);
    }

    private void initUI() {
        tempTV = findViewById(R.id.tv);
        weatherIcon = findViewById(R.id.imv_weather_indicator);
        tempMaxTV = findViewById(R.id.maxTV);
        tempMinTV = findViewById(R.id.minTV);
        precipitationTV = findViewById(R.id.precipitationTV);
        greetingsTV = findViewById(R.id.greetings_flavor);
        dateTV = findViewById(R.id.date_flavor);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.systemDefault());
        String formattedDateText = dateTV.getText().toString().replace("$DATE", Utils.getLocalizedFormattedDate(zonedDateTime));
        dateTV.setText(formattedDateText);

        recyclerView = findViewById(R.id.recycler_view_entries);

        iconCheck = Utils.drawableToBitmap(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_baseline_check_32));
        iconDelete = Utils.drawableToBitmap(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_baseline_delete_sweep_32));
        initAdapterGestures();

        Button btnNewEntry = findViewById(R.id.btn_new_entry);
        btnNewEntry.setOnClickListener(v -> {
            AddOrUpdateEntryDialogFragment dialog = new AddOrUpdateEntryDialogFragment();
            dialog.setMode(1);
            dialog.show(getSupportFragmentManager(), "AddEntryDialog");
        });

    }

    /**
     * Starts the process of requesting the user's permission for accessing the device's location and then creating a
     * WeatherDataRequest for the Location via the WeatherDataProvider.
     */
    private void initAPI() {
        requestLocationPermissions();
    }


    /**
     * If not already given, this method requests the user to allow accessing the device's location.
     */
    private void requestLocationPermissions() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        requestPermissions(permissions, Utils.REQUEST_LOCATION_PERMISSIONS);
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        switch (requestCode) {
            case Utils.REQUEST_LOCATION_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    provider = new WeatherDataProvider(getApplicationContext(), this);
                    provider.update();
                    break;
                } else {
                    tempTV.setText(R.string.missing_permission);
                }
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Sets up a notification channel for the app.
     */
    private void setupNotificationChannel() {
        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper.createNotificationChannel(this,
                getResources().getString(R.string.notification_channel_name),
                getResources().getString(R.string.notification_channel_description),
                NotificationHelper.MAIN_NOTIFICATION_CHANNEL_ID);

    }

    /**
     * Initializes the alarms depending on the settings
     */
    private void initializeAlarms() {

        Alarm alarm = new Alarm();
        if (prefs.getBoolean(Utils.SP_NOTIFICATION_ENABLED_KEY, true)) {
            long triggerAt = Utils.millisForAlarm(prefs.getInt(Utils.SP_NOTIFICATION_TIME_ONE_HOUR_KEY, 7), prefs.getInt(Utils.SP_NOTIFICATION_TIME_ONE_MINUTE_KEY, 0));
            alarm.setRepeatingAlarm(this, triggerAt, AlarmManager.INTERVAL_DAY, Alarm.REQUEST_CODE_MORNING, Alarm.TYPE_MORNING_ALARM);
            triggerAt = Utils.millisForAlarm(prefs.getInt(Utils.SP_NOTIFICATION_TIME_TWO_HOUR_KEY, 7), prefs.getInt(Utils.SP_NOTIFICATION_TIME_TWO_MINUTE_KEY, 0));
            alarm.setRepeatingAlarm(this, triggerAt, AlarmManager.INTERVAL_DAY, Alarm.REQUEST_CODE_EVENING, Alarm.TYPE_EVENING_ALARM);
        }
        long resetTime = Utils.millisForReset();
        alarm.setRepeatingAlarm(this, resetTime, AlarmManager.INTERVAL_DAY * 7, Alarm.REQUEST_CODE_RESET, Alarm.TYPE_RESET_ALARM);
    }


    @Override
    public void onWeatherDataUpdated() {
        Weather weather = provider.getWeather();
        tempTV.setText(weather.getTemperature() + "°C");
        tempMinTV.setText("Min: " + weather.getMinTemp() + "°C");
        tempMaxTV.setText("Max: " + weather.getMaxTemp() + "°C");
        precipitationTV.setText("Niederschlag: " + (int) weather.getPrecipitation() * 100 + "%");
        Log.d("kek", String.valueOf(weather.getPrecipitation()));
        Glide.with(this).load(weather.getImageURL()).into(weatherIcon);
    }

    @Override
    public void onEntryListUpdated() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                entryAdapter.updateEntries(manager.getCurrentEntries());
                entryAdapter.notifyDataSetChanged();
            }
        });

    }


    @Override
    public void onListLoaded() {
        manager.requestUpdate();
    }


    @Override
    public void onEntryEdited(Entry entry, int position) {
        AddOrUpdateEntryDialogFragment dialog = new AddOrUpdateEntryDialogFragment();
        dialog.setMode(-1);
        dialog.setEntry(entry, position);
        dialog.show(getSupportFragmentManager(), "AddEntryDialog");

    }

    /**
     * Sets Swipe-Gestures on the RecyclerView via the ItemTouchHelper class.
     */
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
                Entry deletedEntry = manager.getCurrentEntries().get(position);
                String snackbarUndoString = getResources().getString(R.string.item_undo_snackbar_button);

                switch (direction) {
                    case ItemTouchHelper.LEFT: //ITEM DELETED
                        Entry finalDeletedEntry = deletedEntry;
                        String snackbarDeletedString = getResources().getString(R.string.item_deleted_snackbar).replace("$ITEM", manager.getCurrentEntries().get(viewHolder.getAdapterPosition()).getName());
                        Snackbar.make(recyclerView, snackbarDeletedString, Snackbar.LENGTH_LONG).setAction(snackbarUndoString, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                manager.addEntry(finalDeletedEntry);
                                updateAdapterWithAnimation(position);
                            }
                        }).show();
                        manager.removeEntry(manager.getCurrentEntries().remove(position));
                        updateAdapterWithAnimation(position);
                        break;

                    case ItemTouchHelper.RIGHT: //ITEM DONE
                        finalDeletedEntry = deletedEntry;
                        String snackbarDoneString = getResources().getString(R.string.item_done_snackbar).replace("$ITEM", manager.getCurrentEntries().get(viewHolder.getAdapterPosition()).getName()).replace("$POINTS", String.valueOf(manager.getCurrentEntries().get(viewHolder.getAdapterPosition()).difficulty.points));
                        Snackbar.make(recyclerView, snackbarDoneString, Snackbar.LENGTH_LONG).setAction(snackbarUndoString, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                manager.addEntry(finalDeletedEntry);
                                managePoints(manager.getCurrentEntries().get(position).category, manager.getCurrentEntries().get(position).difficulty, false);
                                updateAdapterWithAnimation(position);
                            }
                        }).show();
                        managePoints(manager.getCurrentEntries().get(position).category, manager.getCurrentEntries().get(position).difficulty, true);
                        manager.removeEntry(manager.getCurrentEntries().remove(position));
                        updateAdapterWithAnimation(position);
                        break;
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
                    int h = iconCheck.getHeight();
                    View itemView = viewHolder.itemView;
                    int itemViewHeight = itemView.getHeight();
                    int dif = itemViewHeight - h;
                    if (dX > 0) {
                        swipeColor.setColor(itemView.getResources().getColor(R.color.easy, null));
                        c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), itemView.getRight(),
                                (float) itemView.getBottom(), swipeColor);
                        c.drawBitmap(iconCheck, (float) itemView.getLeft(), (float) itemView.getTop() + dif / 2, swipeColor);
                    } else {
                        swipeColor.setColor(itemView.getResources().getColor(R.color.hard, null));
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


    @Override
    public void onItemNew(String name, int hour, int minute, Category category, Difficulty difficulty, ZonedDateTime targetDate) {
        Entry entry = new Entry(name, category, difficulty, Utils.millisForEntryCurrentDay(hour, minute));
        manager.addEntry(entry);
    }

    @Override
    public void onItemUpdate(String name, int hour, int minute, Category category, Difficulty difficulty, Entry oldEntry, int position, ZonedDateTime targetDate) {
        manager.removeEntry(oldEntry);
        Entry entry = new Entry(name, category, difficulty, Utils.millisForEntryCurrentDay(hour, minute));
        manager.addEntryAtPosition(position, entry);
        entryAdapter.notifyDataSetChanged();
        Toast.makeText(this, getString(R.string.toast_changes_successful), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNegativeClicked(int mode) {
        if (mode == 1) {
            Toast.makeText(this, getString(R.string.toast_new_entry_discarded), Toast.LENGTH_SHORT).show();
        } else if (mode == -1) {
            Toast.makeText(this, getString(R.string.toast_changes_discarded), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Instead of calling notifyDataSetChanged on the adapter, this method will inform it of the changes.
     * The adapter will move up every Entry in the RecyclerView with an animation instead of refreshing the list.
     *
     * @param position the adapter position of the removed Entry
     */
    private void updateAdapterWithAnimation(int position) {
        entryAdapter.notifyItemRemoved(position);
        entryAdapter.notifyItemRangeChanged(position, manager.getCurrentEntries().size());
        entryAdapter.updateEntries(manager.getCurrentEntries());
    }

    /**
     * Awards points for completion of Entries or removes them from the User's total count.
     * Accesses and modifies the User's total points by calling the SharedPreferences of the according Category.
     *
     * @param category    The Category of the Entry.
     * @param difficulty  The Difficulty of the Entry. Used to determine the amount to be added/removed.
     * @param addOrRemove True = add, False = remove
     */
    private void managePoints(Category category, Difficulty difficulty, Boolean addOrRemove) {

        switch (category) {
            case WORK:
                int currentWorkPoints = prefs.getInt(Utils.SP_WORK_TOTAL_POINTS_KEY, 0);
                if (addOrRemove) {
                    editor.putInt(Utils.SP_WORK_TOTAL_POINTS_KEY, currentWorkPoints + difficulty.points);
                    editor.apply();
                    break;
                } else {
                    editor.putInt(Utils.SP_WORK_TOTAL_POINTS_KEY, currentWorkPoints - difficulty.points);
                    editor.apply();
                    break;
                }
            case HOBBY:
                int currentHobbyPoints = prefs.getInt(Utils.SP_HOBBY_TOTAL_POINTS_KEY, 0);
                if (addOrRemove) {
                    editor.putInt(Utils.SP_HOBBY_TOTAL_POINTS_KEY, currentHobbyPoints + difficulty.points);
                    editor.apply();
                    break;
                } else {
                    editor.putInt(Utils.SP_HOBBY_TOTAL_POINTS_KEY, currentHobbyPoints - difficulty.points);
                    editor.apply();
                    break;
                }
            case FITNESS:
                int currentFitnessPoints = prefs.getInt(Utils.SP_FITNESS_TOTAL_POINTS_KEY, 0);
                if (addOrRemove) {
                    editor.putInt(Utils.SP_FITNESS_TOTAL_POINTS_KEY, currentFitnessPoints + difficulty.points);
                    editor.apply();
                    break;
                } else {
                    editor.putInt(Utils.SP_FITNESS_TOTAL_POINTS_KEY, currentFitnessPoints - difficulty.points);
                    editor.apply();
                    break;
                }
            default:
                break;
        }
    }

}