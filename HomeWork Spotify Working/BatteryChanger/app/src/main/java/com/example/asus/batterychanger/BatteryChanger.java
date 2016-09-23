package com.example.asus.batterychanger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ASUS on 23.9.2016 г..
 */
public class BatteryChanger extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int batteryLevel = intent.getIntExtra(
                BatteryManager.EXTRA_LEVEL, 0);
        int maxLevel = intent
                .getIntExtra(BatteryManager.EXTRA_SCALE, 0);
        int batteryHealth = intent.getIntExtra(
                BatteryManager.EXTRA_HEALTH,
                BatteryManager.BATTERY_HEALTH_UNKNOWN);
        float batteryPercentage = ((float) batteryLevel / (float) maxLevel) * 100;
        Log.d("BatteryHealth", String.valueOf(batteryHealth));
        Log.d("BatteryPercentage", String.valueOf(batteryPercentage));
        Toast.makeText(context, String.valueOf(batteryPercentage), Toast.LENGTH_SHORT).show();
    }
}
