package com.ristudios.personalagent.ui.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.preference.PreferenceManager;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.preference.PreferenceManager;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.ristudios.personalagent.R;
import com.ristudios.personalagent.data.Category;
import com.ristudios.personalagent.data.Difficulty;
import com.ristudios.personalagent.data.Entry;
import com.ristudios.personalagent.data.EntryManager;
import com.ristudios.personalagent.data.api.Weather;
import com.ristudios.personalagent.data.api.WeatherDataListener;
import com.ristudios.personalagent.data.api.WeatherDataProvider;
import com.ristudios.personalagent.ui.adapter.EntryAdapter;
import com.ristudios.personalagent.ui.fragments.AddEntryDialogFragment;
import com.ristudios.personalagent.utils.Utils;
import com.ristudios.personalagent.utils.notifications.Alarm;
import com.ristudios.personalagent.utils.notifications.NotificationHelper;


import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;



/**
 * LauncherActivity, shows tasks for current day as well as weather information.
 */


public class MainActivity extends BaseActivity implements WeatherDataListener, EntryManager.EntryManagerListener, AddEntryDialogFragment.AddEntryDialogClickListener, EntryAdapter.OnEntryEditedListener {


    //For API-testing
    private WeatherDataProvider provider;
    private TextView tempTV, tempMaxTV, tempMinTV, precipitationTV;
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


    private void initData() {
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        manager = new EntryManager(this, this);
        entryAdapter = new EntryAdapter(this, this);
        recyclerView.setAdapter(entryAdapter);
    }


    //TODO: SUBJECT TO CHANGE! only for testing purposes - nothing final
    private void initUI() {
        tempTV = findViewById(R.id.tv);
        weatherIcon = findViewById(R.id.imageEEEE);
        tempMaxTV = findViewById(R.id.maxTV);
        tempMinTV = findViewById(R.id.minTV);
        precipitationTV = findViewById(R.id.precipitationTV);
        recyclerView = findViewById(R.id.recycler_view_entries);

        iconCheck = Utils.drawableToBitmap(getDrawable(R.drawable.ic_baseline_check_32));
        iconDelete = Utils.drawableToBitmap(getDrawable(R.drawable.ic_baseline_delete_sweep_32));
        initAdapterGestures();

        FloatingActionButton fabAddEntry = findViewById(R.id.fab_add_entry);
        fabAddEntry.setOnClickListener(v -> {
            DialogFragment dialog = new AddEntryDialogFragment();
            dialog.show(getSupportFragmentManager(), "AddEntryDialog");
        });

    }

    private void initAPI() {
        requestLocationPermissions();
        provider = new WeatherDataProvider(getApplicationContext(), this);
        provider.update();
    }


    //TODO: Request Permissions outside of OnCreate() or check if they were denied
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
                    break;
                }
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

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

        //TODO: fix app so alarms will be cancelled when user changes prefs in settings
        Alarm alarm = new Alarm();
        if (prefs.getBoolean(Utils.SP_NOTIFICATION_ENABLED_KEY, true)) {
            long triggerAt = Utils.millisForAlarm(prefs.getInt(Utils.SP_NOTIFICATION_TIME_ONE_HOUR_KEY, 7), prefs.getInt(Utils.SP_NOTIFICATION_TIME_ONE_MINUTE_KEY, 0));

            alarm.setRepeatingAlarm(this, triggerAt, AlarmManager.INTERVAL_DAY, Alarm.REQUEST_CODE_MORNING, Alarm.TYPE_MORNING_ALARM);

            triggerAt = Utils.millisForAlarm(prefs.getInt(Utils.SP_NOTIFICATION_TIME_TWO_HOUR_KEY, 7), prefs.getInt(Utils.SP_NOTIFICATION_TIME_TWO_MINUTE_KEY, 0));
            alarm.setRepeatingAlarm(this, triggerAt, AlarmManager.INTERVAL_DAY, Alarm.REQUEST_CODE_EVENING, Alarm.TYPE_EVENING_ALARM);
            Log.d(Utils.LOG_ALARM, "Alarm set for " + prefs.getInt(Utils.SP_NOTIFICATION_TIME_ONE_HOUR_KEY, 7) + ":" + prefs.getInt(Utils.SP_NOTIFICATION_TIME_ONE_MINUTE_KEY, 0));
            Log.d(Utils.LOG_ALARM, "Alarm set for " + prefs.getInt(Utils.SP_NOTIFICATION_TIME_TWO_HOUR_KEY, 7) + ":" + prefs.getInt(Utils.SP_NOTIFICATION_TIME_TWO_MINUTE_KEY, 0));
        }

    }

    //TODO: pretty ugly, make it just pretty
    @Override
    public void onWeatherDataUpdated() {
        Weather weather = provider.getWeather();
        tempTV.setText(weather.getTemperature() + "°C");
        tempMinTV.setText("Min: " + weather.getMinTemp() + "°C");
        tempMaxTV.setText("Max: " + weather.getMaxTemp() + "°C");
        precipitationTV.setText("Niederschlag: " + (int) weather.getPrecipitation() + "%");
        Glide.with(this).load(weather.getImageURL()).into(weatherIcon);
    }

    @Override
    public void onEntryListUpdated() {
        entryAdapter.updateEntries(manager.getCurrentEntries());
    }



    //TODO: FÜR PATRICK
    @Override
    public void onEntryEdited(Entry entry, int position) {

    }

    private void initAdapterGestures(){
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Entry deletedEntry = manager.getCurrentEntries().get(position);

                switch (direction){
                    case ItemTouchHelper.LEFT:
                        Entry finalDeletedEntry = deletedEntry;
                        Snackbar.make(recyclerView, manager.getCurrentEntries().get(viewHolder.getAdapterPosition()).getName() + " deleted!", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                manager.addEntry(finalDeletedEntry);
                                entryAdapter.notifyItemRemoved(position);
                                entryAdapter.notifyItemRangeChanged(position, manager.getCurrentEntries().size());
                                entryAdapter.updateEntries(manager.getCurrentEntries());
                            }
                        }).show();
                        manager.removeEntry(manager.getCurrentEntries().remove(position));
                        entryAdapter.notifyItemRemoved(position);
                        entryAdapter.notifyItemRangeChanged(position, manager.getCurrentEntries().size());
                        entryAdapter.updateEntries(manager.getCurrentEntries());
                        break;

                    case ItemTouchHelper.RIGHT:
                        finalDeletedEntry = deletedEntry;
                        Snackbar.make(recyclerView, manager.getCurrentEntries().get(viewHolder.getAdapterPosition()).getName()+ " completed! You've earned " + manager.getCurrentEntries().get(viewHolder.getAdapterPosition()).getDifficulty().points + " points!", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                manager.addEntry(finalDeletedEntry);
                                entryAdapter.notifyItemRemoved(position);
                                entryAdapter.notifyItemRangeChanged(position, manager.getCurrentEntries().size());
                                entryAdapter.updateEntries(manager.getCurrentEntries());
                            }
                        }).show();
                        manager.removeEntry(manager.getCurrentEntries().remove(position));
                        entryAdapter.notifyItemRemoved(position);
                        entryAdapter.notifyItemRangeChanged(position, manager.getCurrentEntries().size());
                        entryAdapter.updateEntries(manager.getCurrentEntries());
                        break;
                }
            }

            @Override
            public void onChildDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    // Get RecyclerView item from the ViewHolder
                    View itemView = viewHolder.itemView;
                    if (dX > 0) {

                        swipeColor.setColor(itemView.getResources().getColor(R.color.easy, null));
                        // Draw Rect with varying right side, equal to displacement dX
                        c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                                (float) itemView.getBottom(), swipeColor);
                        c.drawBitmap(iconCheck, (float) itemView.getLeft(), (float) itemView.getTop(), swipeColor);
                    } else {
                        /* Set your color for negative displacement */
                        swipeColor.setColor(itemView.getResources().getColor(R.color.hard, null));
                        // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                        c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                (float) itemView.getRight(), (float) itemView.getBottom(), swipeColor);
                        c.drawBitmap(iconDelete, (float) itemView.getLeft() + itemView.getWidth() -100, (float) itemView.getTop(), swipeColor);

                    }

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }


    @Override
    public void onPositiveClicked(String name, int hour, int minute, Category category, Difficulty difficulty) {
        Entry entry = new Entry(name, category, difficulty, Utils.millisForEntry(hour, minute));
        manager.addEntry(entry);
        Log.d("JAVA_TIME", "Item added with time " + Utils.getFormattedDateTime(Utils.getDateFromMillis(entry.getDate())));
    }

    @Override
    public void onNegativeClicked() {
        Toast.makeText(this, "Eintrag verworfen", Toast.LENGTH_SHORT).show();
    }

}