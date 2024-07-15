package com.hridoykrisna.jewellerycalculatoradmin.location;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class NetworkLocationWorker extends Worker {
    private static final String TAG = "NetworkLocationWorker";
   /* private FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference databaseReference;
*/
    public NetworkLocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        /*fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        databaseReference = FirebaseDatabase.getInstance().getReference("locations");*/
    }

    @NonNull
    @Override
    public Result doWork() {
        /*if (isConnected()) {
            getCurrentLocation();
        }
        return Result.success();*/

        Intent serviceIntent = new Intent(getApplicationContext(), NetworkLocationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(serviceIntent);
        } else {
            getApplicationContext().startService(serviceIntent);
        }
        return Result.success();
    }

    /*private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && ((NetworkInfo) activeNetwork).isConnectedOrConnecting();
        Log.d(TAG, "Network connected: " + isConnected);
        return isConnected;
    }

    private void getCurrentLocation() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                saveLocationToFirebase(location);
                            }
                        }
                    });
        } catch (SecurityException e) {
            Log.e(TAG, "Location permission not granted", e);
        }
    }

    private void saveLocationToFirebase(Location location) {
        String userId = "unique_user_id"; // Replace with actual user ID
        Map<String, Object> locationData = new HashMap<>();
        locationData.put("Class", "NetworkLocationWorker");
        locationData.put("latitude", location.getLatitude());
        locationData.put("longitude", location.getLongitude());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, hh:mm:ss aa, dd-MM-yyyy");
        locationData.put("timestamp", simpleDateFormat.format(System.currentTimeMillis()));

        databaseReference.child(userId).setValue(locationData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Location saved to Firebase");
                    } else {
                        Log.e(TAG, "Failed to save location to Firebase", task.getException());
                    }
                });
    }*/
}
