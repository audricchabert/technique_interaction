package com.example.user.findtheword;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by renevier-gonin on 08/10/2014.
 */
public class MyAcceleroListener  implements SensorEventListener {

    final             float marge = 0.3f;
    final int PAS = 1;
    final int PAS_MAX = 15;

    private final Display display;
    private final RelativeLayout parent;
    private Sensor mAccelerometer;
    private SensorManager mSensorManager;
    View view;

    // les 3 valeurs de l'accélérometre
    private float x;
    private float y;
    private float z;

    int multiX = 0;
    int multiY = 0;

    private float[]  gravity;
    private final float alpha = 0.8f; // http://developer.android.com/reference/android/hardware/SensorEvent.html
    long timestamp = -1;

    float vx = 0;
    private RelativeLayout.LayoutParams params;

    public boolean isRunning() {
        return running;
    }

    boolean running = false;

    int img_width;
    int img_heiht;

    public MyAcceleroListener(SensorManager sm, View v, Display d, int width, int height) {
        this.mSensorManager = sm;
        this.view = v;
        this.display = d;
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        parent = (RelativeLayout) view.getParent() ;

        img_width = width;
        img_heiht = height;

        /* pour le déplacement de la fleche, cela se fait avec les marges */
        params = new RelativeLayout.LayoutParams(img_width,img_heiht);
        params.leftMargin =0;
        params.topMargin = 0;

        view.setLayoutParams(params);
        view.invalidate();
    }

    public void stop() {
        mSensorManager.unregisterListener(this);
        running = false;
    }

    public void start() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        running = true;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;


        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                x = sensorEvent.values[0];
                y = sensorEvent.values[1];
                break;
            case Surface.ROTATION_90:
                x = -sensorEvent.values[1];
                y = sensorEvent.values[0];
                break;
            case Surface.ROTATION_180:
                x = -sensorEvent.values[0];
                y = -sensorEvent.values[1];
                break;
            case Surface.ROTATION_270:
                x = sensorEvent.values[1];
                y = -sensorEvent.values[0];
                break;
        }
        // z = sensorEvent.values[2];


        /********* interprétation des valeurs ... ici avec un langage arbitraire **********/
        /********* avec une pseudo accélération/intertie du mouvement            **********/
        if (x < -marge) {
            multiX = Math.max(-PAS_MAX, multiX-1);
        }
        else if (x > marge) {
            multiX = Math.min(PAS_MAX, multiX+1);
        }
        else {
            if (multiX > 0) multiX = multiX-1;
            else if (multiX < 0) multiX = multiX-1;
        }
        params.leftMargin = Math.max(0, Math.min(params.leftMargin - PAS * multiX, parent.getMeasuredWidth() - img_width));



        if (y > marge) {
            multiY = Math.min(PAS_MAX, multiY+1);
        }
        else if (y < -marge) {
            multiY = Math.max(-PAS_MAX, multiY-1);
        }
        else {
            if (multiY > 0) multiY = multiY-1;
            else if (multiY < 0) multiY = multiY-1;
        }

        params.topMargin = Math.max(0, Math.min(params.topMargin + PAS * multiY, parent.getMeasuredHeight() - img_heiht));

        view.setLayoutParams(params);
        view.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
