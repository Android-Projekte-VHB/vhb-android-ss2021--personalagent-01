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

    public CoordinatesProvider(Context context, CoordinatesListener listener) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        this.listener = listener;
    }

    public void start() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, token.getToken()).addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                listener.onCoordinatesReady(location.getLongitude(), location.getLatitude());
            }
        });
    }

    interface CoordinatesListener{
        void onCoordinatesReady(double longitude, double latitude);
    }

}
