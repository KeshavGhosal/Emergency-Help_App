package com.example.help_app;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView tvName, tvBlood, tvCountdown;
    private Button btnSOS, btnCancel, btnFriend1, btnFriend2, btnFriend3;
    private boolean isSosTriggered = false;
    private CountDownTimer timer;
    private SharedPreferences prefs;
    private FusedLocationProviderClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationClient = LocationServices.getFusedLocationProviderClient(this);
        prefs = getSharedPreferences("EmergencyPrefs", MODE_PRIVATE);

        tvName = findViewById(R.id.tvNameDisplay);
        tvBlood = findViewById(R.id.tvBloodDisplay);
        tvCountdown = findViewById(R.id.tvCountdown);
        btnSOS = findViewById(R.id.btnSOS);
        btnCancel = findViewById(R.id.btnCancel);

        btnFriend1 = findViewById(R.id.btnPolice);
        btnFriend2 = findViewById(R.id.btnFire);
        btnFriend3 = findViewById(R.id.btnAmbulance);

        btnFriend1.setText("Fire");
        btnFriend2.setText("Police");
        btnFriend3.setText("Med");

        tvName.setText(prefs.getString("name", "No Profile"));
        tvBlood.setText("Blood Type: " + prefs.getString("blood", "Unknown"));

        // Alert Listeners with real phone numbers
        btnFriend1.setOnClickListener(v -> dispatchFullAlert("<firebrigade_number>", "Fire"));
        btnFriend2.setOnClickListener(v -> dispatchFullAlert("Police_number", "Police"));
        btnFriend3.setOnClickListener(v -> dispatchFullAlert("Medical_number", "Med"));

        findViewById(R.id.btnAddProfile).setOnClickListener(v -> {
            Intent i = new Intent(this, SetupActivity.class);
            i.putExtra("isAddingNew", true);
            startActivity(i);
        });

        findViewById(R.id.btnEditProfile).setOnClickListener(v -> {
            prefs.edit().clear().apply();
            startActivity(new Intent(this, SetupActivity.class));
            finish();
        });

        btnSOS.setOnClickListener(v -> startSOS());
        btnCancel.setOnClickListener(v -> stopSOS());

        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sm != null) sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        requestPermissions();
    }

    private void dispatchFullAlert(String phoneNumber, String label) {
        // 1. Voice Call (Immediate)
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(callIntent);
            }
        } catch (Exception e) { Toast.makeText(this, "Call failed", Toast.LENGTH_SHORT).show(); }

        // 2. SMS + Location (Async)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationClient.getLastLocation().addOnCompleteListener(task -> {
                String msg = "EMERGENCY: " + prefs.getString("name", "User") + " needs help!";
                if (task.isSuccessful() && task.getResult() != null) {
                    msg += "\nLocation: https://www.google.com/maps?q=" + task.getResult().getLatitude() + "," + task.getResult().getLongitude();
                } else {
                    msg += "\n(GPS location unavailable)";
                }

                sendSms(phoneNumber, msg, label);
            });
        }
    }

    private void sendSms(String number, String message, String label) {
        try {
            SmsManager smsManager = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S)
                    ? getSystemService(SmsManager.class) : SmsManager.getDefault();
            ArrayList<String> parts = smsManager.divideMessage(message);
            smsManager.sendMultipartTextMessage(number, null, parts, null, null);
            Toast.makeText(this, "SMS sent to " + label, Toast.LENGTH_SHORT).show();
        } catch (Exception e) { Toast.makeText(this, "SMS failed", Toast.LENGTH_SHORT).show(); }
    }

    private void startSOS() {
        isSosTriggered = true;
        btnSOS.setVisibility(View.GONE);
        tvCountdown.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
        timer = new CountDownTimer(10000, 1000) {
            public void onTick(long ms) { tvCountdown.setText(String.valueOf(ms / 1000)); }
            public void onFinish() {
                String contact = prefs.getString("contact1", "");
                if (!contact.isEmpty()) dispatchFullAlert(contact, "Emergency Contact");
                stopSOS();
            }
        }.start();
    }

    private void stopSOS() {
        if (timer != null) timer.cancel();
        isSosTriggered = false;
        tvCountdown.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
        btnSOS.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double g = Math.sqrt(event.values[0]*event.values[0] + event.values[1]*event.values[1] + event.values[2]*event.values[2]) / 9.8;
        if (g > 15.0 && !isSosTriggered) startSOS();
    }

    @Override public void onAccuracyChanged(Sensor s, int a) {}

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE
        }, 1);
    }
}
