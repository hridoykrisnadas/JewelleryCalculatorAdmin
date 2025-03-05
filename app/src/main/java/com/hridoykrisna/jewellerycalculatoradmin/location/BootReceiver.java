package com.hridoykrisna.jewellerycalculatoradmin.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.hridoykrisna.jewellerycalculatoradmin.Scrap.DailyUpdateWorker;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, NetworkLocationService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            } else {
                context.startService(serviceIntent);
            }

//            // Schedule the periodic work again
//            PeriodicWorkRequest dailyWorkRequest = new PeriodicWorkRequest.Builder(
//                    DailyUpdateWorker.class, 1, TimeUnit.DAYS)
//                    .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
//                    .build();
//
//            WorkManager.getInstance(context).enqueue(dailyWorkRequest);
        }
    }

    private long calculateInitialDelay() {
        // Calculate the delay until 12:01 am
        long currentTime = System.currentTimeMillis();
        long targetTime = new Date().getTime(); // Set to 12:01 am in your timezone
        return targetTime > currentTime ? targetTime - currentTime : targetTime + 86400000 - currentTime;
    }
}
