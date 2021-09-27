package com.ristudios.personalagent.data.api;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnSuccessListener;

public class CoordinatesProvider {

    private final Context context;
    private FusedLocationProviderClient fusedLocationClient;
    private final CancellationTokenSource token = new CancellationTokenSource();
    private final CoordinatesListener listener;


    /**
     * Creates a new Instance of the CoordinatesProvider Class.
     * If permissions were granted, It accesses the current Location of the Device in Coordinates of Longitude and Latitude via Android's FusedLocationProviderClient.
     *
     * @param context  The Activity's Context
     * @param listener A CoordinatesListener that will receive the current Location when ready
     */

    public CoordinatesProvider(Context context, CoordinatesListener listener) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        this.listener = listener;
    }

    /**
     * Starts the CoordinatesProvider. On Success, it notifies the CoordinatesListener and provides it the Devices current Location.
     */
    public void start() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
            //Missing Permissions are handled in MainActivity
        }
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, token.getToken()).addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                listener.onCoordinatesReady(location.getLongitude(), location.getLatitude());
            }
        });
    }

    /**
     * Implementation of this Interface will notify the Class when the CoordinatesProvider has successfully accessed the Device's Location.
     */
    interface CoordinatesListener {
        /**
         * Override to work with the results of the CoordinatesProvider.
         *
         * @param longitude Current Location's Longitude
         * @param latitude  Current Location's Latitude
         */
        void onCoordinatesReady(double longitude, double latitude);
    }

}
