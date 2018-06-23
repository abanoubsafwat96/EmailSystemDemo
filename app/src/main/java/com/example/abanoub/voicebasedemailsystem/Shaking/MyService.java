package com.example.abanoub.voicebasedemailsystem.Shaking;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import com.example.abanoub.voicebasedemailsystem.SignInActivity;
import com.example.abanoub.voicebasedemailsystem.SplashActivity;

public class MyService  extends Service implements SensorEventListener {

    SensorManager sensorManager;
    Prefs prefs;

    @Override
    public void onCreate() {
        super.onCreate();

        startForeground(1, new Notification());
        SplashActivity.isServiceRunning = true;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        prefs = new Prefs(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startForeground(1, new Notification());
        SplashActivity.isServiceRunning = true;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        prefs = new Prefs(this);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SplashActivity.isServiceRunning = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];
        float accelationSquareRoot = (x * x + y * y + z * z) / 50;
        if (accelationSquareRoot >= Integer.parseInt(prefs.getStringPrefs(
                Prefs.sensitivity, "8"))) {
            Intent i = getPackageManager().getLaunchIntentForPackage(
                    prefs.getStringPrefs(Prefs.appPackage,
                            "com.example.abanoub.voicebasedemailsystem"));
            if (i != null) {
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
