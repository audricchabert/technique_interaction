package com.example.clement.themaze;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_the_maze)
public class TheMaze extends Activity implements SensorEventListener {

    @ViewById
    Button buttonSwitchView;

    @ViewById
    MazeView mazeView;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;

    private boolean map=true;
    @Click
    public void buttonSwitchView(){
        mazeView.setMapView();
        map=!map;
        if (map){
            sensorManager.unregisterListener(this);

        }
        else{
            sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        }
    }

    SensorManager sensorManager;
    Display d;
    private Sensor mAccelerometer;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        d = wm.getDefaultDisplay();

    }

    @AfterViews
    public void initButton(){
        buttonSwitchView.setText("Switch View");
        mazeView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;


        if (mySensor.getType()==Sensor.TYPE_ACCELEROMETER){
            float x = event.values[0];
            float y = event.values[1];
            Log.e("TAG", x + " " + y);

            switch(d.getRotation()) {
                case Surface.ROTATION_0:
                    x = event.values[0];
                    y = event.values[1];
                    break;
                case Surface.ROTATION_90:
                    x = -event.values[1];
                    y = event.values[0];
                    break;
                case Surface.ROTATION_180:
                    x = -event.values[0];
                    y = -event.values[1];
                    break;
                case Surface.ROTATION_270:
                    x = event.values[1];
                    y = -event.values[0];
                    break;
            }
            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 10) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                last_x = x;
                last_y = y;
                //mazeView.changeXY(x, y);
                mazeView.changeXY(x, y);
                Log.e("TAG",x+" "+ y);
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);


    }

}
