package com.hridoykrisna.jewellerycalculatoradmin.Scrap;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.hridoykrisna.jewellerycalculatoradmin.LivePrice;
import com.hridoykrisna.jewellerycalculatoradmin.notification.TokenManager;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class DailyUpdateWorker extends Worker {
    LivePrice livePrice;

    public DailyUpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {

            livePrice = new LivePrice();WebScraper.runTask();

            // Step 2: Check current Firebase value
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("live_price").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    LivePrice currentLivePrice = snapshot.getValue(LivePrice.class);
                    if (currentLivePrice != null && livePrice.getGold_gram_22() != 0) {
                        if (currentLivePrice.getGold_gram_22() != livePrice.getGold_gram_22()){
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, hh:mm aa, dd:MM:yyyy");
                            livePrice.setUpdate_at(simpleDateFormat.format(System.currentTimeMillis()));

                            // Create a SimpleDateFormat with the desired format
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                            // Get today's date
                            Calendar calendar = Calendar.getInstance();
                            String todayDate = dateFormat.format(calendar.getTime());

                            // Get yesterday's date
                            calendar.add(Calendar.DATE, -1); // Subtract 1 day
                            String yesterdayDate = dateFormat.format(calendar.getTime());

                            livePrice.setUpdate_date(yesterdayDate);
                            livePrice.setRunning_date(todayDate);
                            livePrice.setUpdate_by("Automatic App Update Worker");

                            mDatabase.child("live_price").setValue(livePrice).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    sendNotification(todayDate);
                                }
                            });
                        } else {
                            Log.d("Worker Update Price", "Price are same: "+currentLivePrice.getGold_gram_22());
                        }
                    }
                     else {
                         Log.d("Worker Update Price", "Price are same: " + currentLivePrice.getGold_gram_22());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }

    private void sendNotification(String todayDate) {
        String[] accessToken = new String[1];

        TokenManager.getAccessToken(getApplicationContext(), new TokenManager.TokenCallback() {
            @Override
            public void onTokenReceived(String token) throws Exception {
                // Use the token here (on the main thread)
                accessToken[0] = token;
                Log.d("AccessToken", "Token: " + accessToken[0]);

                notificationByClient(todayDate, accessToken[0]);
            }

            @Override
            public void onError(Exception e) {
                Log.e("AccessToken", "Error generating token: " + e.getMessage());
            }
        });
    }

    private void notificationByClient(String todayDate, String serverKey) throws Exception {

        String fcmUrl = "https://fcm.googleapis.com/v1/projects/jewellery-calculator-27c66/messages:send";

        OkHttpClient client = new OkHttpClient();

        String title = "বাজুস নির্ধারিত স্বর্ণের নতুন মূল্য নির্ধারিত";
        String body = todayDate + " তারিখ থেকে ২২ ক্যারেট স্বর্ণের নতুন দাম: " + NumberFormat.getInstance().format(Math.round(livePrice.getGold_gram_22() * 11.664)) + " টাকা মাত্র।";

        // Inner data map
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("title", title);
        dataMap.put("message", body);

        // Message map
        HashMap<String, Object> messageMap = new HashMap<>();
        messageMap.put("topic", "notification");
        messageMap.put("data", dataMap); // Nested map

        // Outer map
        HashMap<String, Object> rootMap = new HashMap<>();
        rootMap.put("message", messageMap);

        Gson gson = new Gson();
        String jsonPayload = gson.toJson(rootMap);

        RequestBody bodyRequest = RequestBody.create(
                jsonPayload,
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(fcmUrl)
                .post(bodyRequest)
                .addHeader("Authorization", "Bearer " + serverKey)
                .addHeader("Content-Type", "application/json")
                .build();

        new Thread(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    System.out.println("Message sent successfully: " + response.body().string());
                } else {
                    System.out.println("Failed to send message: " + response.body().string());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

}
