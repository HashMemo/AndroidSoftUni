package com.example.asus.batterychanger;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BatteryChanger batteryChanger = new BatteryChanger();
        registerReceiver(batteryChanger, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
}
