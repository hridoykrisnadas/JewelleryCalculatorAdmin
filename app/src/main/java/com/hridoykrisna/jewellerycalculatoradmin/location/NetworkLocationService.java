package com.hridoykrisna.jewellerycalculatoradmin.location;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hridoykrisna.jewellerycalculatoradmin.MainActivity;
import com.hridoykrisna.jewellerycalculatoradmin.R;

import java.security.Provider;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class NetworkLocationService extends Service {
    private static final String TAG = "NetworkLocationService";
    private static final String CHANNEL_ID = "NetworkLocationServiceChannel";
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference databaseReference;


    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("locations");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Network Location Service")
                .setContentText("Running in the background")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        // Perform the background task here
        checkNetworkAndLocation();

        return START_NOT_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Network Location Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    private void checkNetworkAndLocation() {
        if (isConnected()) {
            getCurrentLocation();
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
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
        locationData.put("Class", "NetworkLocationService");
        locationData.put("latitude", location.getLatitude());
        locationData.put("longitude", location.getLongitude());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, hh:mm:ss aa, dd-MM-yyyy");
        locationData.put("timestamp", simpleDateFormat.format(System.currentTimeMillis()));

        databaseReference.child(userId).setValue(locationData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Location saved to Firebase");
                        Toast.makeText(getApplicationContext(), "Location", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Failed to save location to Firebase", task.getException());
                    }
                });
    }
}
