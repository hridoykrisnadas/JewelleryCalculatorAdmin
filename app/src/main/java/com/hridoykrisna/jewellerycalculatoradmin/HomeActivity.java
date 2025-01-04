package com.hridoykrisna.jewellerycalculatoradmin;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.hridoykrisna.jewellerycalculatoradmin.notification.TokenManager;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {
    private float gold_gram_22;
    private float gold_gram_21;
    private float gold_gram_18;
    private float gold_gram_sonaton;
    private float silver_gram_22;
    private float silver_gram_21;
    private float silver_gram_18;
    private float silver_gram_sonaton;
    private CheckBox isNotificationCB;
    private boolean isNotification;

    private EditText gold22ET, gold21Et, gold18Et, goldSonatonEt, silver22ET, silver21Et, silver18Et, silverSonatonEt;

    private Button updateBtnId;
    private TextView updateDateTv, runningDateTv;
    private String updateDate, runningDate, updateAt;

    private void selectUpdatDate(Context context) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, updateDateListener, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void selectRunningDate(Context context) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, runningDateListener, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    //date set Listener...
    public DatePickerDialog.OnDateSetListener runningDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            calendar.set(year, month, dayOfMonth);
            runningDate = simpleDateFormat.format(calendar.getTime());
            runningDateTv.setText(runningDate);



        }
    };

    public DatePickerDialog.OnDateSetListener updateDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            calendar.set(year, month, dayOfMonth);
            updateDate = simpleDateFormat.format(calendar.getTime());
            updateDateTv.setText(updateDate);

        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        gold22ET = findViewById(R.id.gold22PriceEtId);
        gold21Et = findViewById(R.id.gold21PriceEtId);
        gold18Et = findViewById(R.id.gold18PriceEtId);
        goldSonatonEt = findViewById(R.id.goldTraditonalPriceEtId);
        silver22ET = findViewById(R.id.silver22PriceEtId);
        silver21Et = findViewById(R.id.silver21PriceEtId);
        silver18Et = findViewById(R.id.silver18PriceEtId);
        silverSonatonEt = findViewById(R.id.silverTraditonalPriceEtId);
        updateBtnId = findViewById(R.id.updatePriceBtnId);
        updateDateTv = findViewById(R.id.updateDateTVId);
        runningDateTv = findViewById(R.id.runningDateTvid);
        isNotificationCB = findViewById(R.id.isNotificationCBId);

        updateDateTv.setOnClickListener(v -> {
            selectUpdatDate(HomeActivity.this);
        });
        runningDateTv.setOnClickListener(v -> {
            selectRunningDate(HomeActivity.this);
        });
        
        isNotificationCB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                isNotification = true;
            } else {
                isNotification = false;
            }
        });

        mDatabase.child("live_price").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LivePrice livePrice = snapshot.getValue(LivePrice.class);
                assert livePrice != null;
                gold_gram_22 = livePrice.getGold_gram_22();
                gold_gram_21 = livePrice.getGold_gram_21();
                gold_gram_18 = livePrice.getGold_gram_18();
                gold_gram_sonaton = livePrice.getGold_gram_sonaton();
                silver_gram_22 = livePrice.getSilver_gram_22();
                silver_gram_21 = livePrice.getSilver_gram_21();
                silver_gram_18 = livePrice.getSilver_gram_18();
                silver_gram_sonaton = livePrice.getSilver_gram_sonaton();
                updateDate = livePrice.getUpdate_date();
                runningDate = livePrice.getRunning_date();

                gold22ET.setText("" + gold_gram_22);
                gold21Et.setText("" + gold_gram_21);
                gold18Et.setText("" + gold_gram_18);
                goldSonatonEt.setText("" + gold_gram_sonaton);
                silver22ET.setText("" + silver_gram_22);
                silver21Et.setText("" + silver_gram_21);
                silver18Et.setText("" + silver_gram_18);
                silverSonatonEt.setText("" + silver_gram_sonaton);
                updateDateTv.setText(updateDate);
                runningDateTv.setText(runningDate);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateBtnId.setOnClickListener(v -> {
            if (gold22ET.length() != 0) {
                gold_gram_22 = Float.parseFloat(gold22ET.getText().toString());
            }
            if (gold21Et.length() != 0) {
                gold_gram_21 = Float.parseFloat(gold21Et.getText().toString());
            }
            if (gold18Et.length() != 0) {
                gold_gram_18 = Float.parseFloat(gold18Et.getText().toString());
            }
            if (goldSonatonEt.length() != 0) {
                gold_gram_sonaton = Float.parseFloat(goldSonatonEt.getText().toString());
            }
            if (silver22ET.length() != 0) {
                silver_gram_22 = Float.parseFloat(silver22ET.getText().toString());
            }
            if (silver21Et.length() != 0) {
                silver_gram_21 = Float.parseFloat(silver21Et.getText().toString());
            }
            if (silver18Et.length() != 0) {
                silver_gram_18 = Float.parseFloat(silver18Et.getText().toString());
            }
            if (silverSonatonEt.length() != 0) {
                silver_gram_sonaton = Float.parseFloat(silverSonatonEt.getText().toString());
            }
            if (updateDate.isEmpty()) {
                Toast.makeText(this, "Please select Update Date", Toast.LENGTH_SHORT).show();
            }
            if (runningDate.isEmpty()) {
                Toast.makeText(this, "Please select Running Date", Toast.LENGTH_SHORT).show();
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, hh:mm aa, dd:MM:yyyy");
            updateAt = simpleDateFormat.format(System.currentTimeMillis());

            LivePrice livePrice = new LivePrice(updateDate, runningDate, gold_gram_22, gold_gram_21, gold_gram_18, gold_gram_sonaton, silver_gram_22, silver_gram_21, silver_gram_18, silver_gram_sonaton, updateAt);

            mDatabase.child("live_price").setValue(livePrice).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(HomeActivity.this, "Price Update Successful", Toast.LENGTH_SHORT).show();

                        if (isNotification){
                            sendNotification();
                        }
                    }
                }
            });

        });
    }

    private void sendNotification() {
        String[] accessToken = new String[1];

        TokenManager.getAccessToken(this, new TokenManager.TokenCallback() {
            @Override
            public void onTokenReceived(String token) throws Exception {
                // Use the token here (on the main thread)
                accessToken[0] = token;
                Log.d("AccessToken", "Token: " + accessToken[0]);

                notificationByClient(accessToken[0]);
            }

            @Override
            public void onError(Exception e) {
                Log.e("AccessToken", "Error generating token: " + e.getMessage());
            }
        });

    }


    private void notificationByClient(String serverKey) throws Exception {

        String fcmUrl = "https://fcm.googleapis.com/v1/projects/jewellery-calculator-27c66/messages:send";

        OkHttpClient client = new OkHttpClient();

        String title = "সোনার নতুন মূল্য নির্ধারিত";
        String body = runningDate + " থেকে ২২ ক্যারেট সোনার নতুন দাম: " + NumberFormat.getInstance().format(Math.round(gold_gram_22 * 11.664)) + " টাকা মাত্র";

        // Inner data map
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("title", title);
        dataMap.put("message", body);
        dataMap.put("from", "Notification");

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