package com.example.user.findtheword;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;


public class DemoAccelero extends Activity {

    private MyAcceleroListener myListener;

    /*
        Sensor manager permet de lier un sensor a une action
        un sensor est juste la définition du sensor
     */
    SensorManager sm;
    Sensor s;
    SensorEventListener audricListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_accelero);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();

        /*
        Init du sensor manager et du sensor
         */
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        s = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        View v = findViewById(R.id.imageView);

        Drawable image = getResources().getDrawable(R.drawable.fleche);
        int h = image.getIntrinsicHeight();
        int w = image.getIntrinsicWidth();

        myListener = new MyAcceleroListener(sm, v, d, w, h);


        //
        audricListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] data  = event.values;
                float x = data[0];
                float y = data[1];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }


    public void launchStop(View button) {
        /* listener du bouton, association faite dans le xml */
        if (myListener.isRunning()) myListener.stop();
        else myListener.start();

               /*

        On enregistre le listener perso sur le sensor s avec le delay fastest
         */
        sm.registerListener(audricListener,s,SensorManager.SENSOR_DELAY_FASTEST);

    }




    /* stop en cas de quit... */
    /* il faudra relancer l'écoute en cliquant sur le bouton */
    protected void onPause() {
        super.onPause();



        /*
            On unregister le listener sur le sensor s
         */
        sm.unregisterListener(audricListener, s);

        myListener.stop();
    }
    protected void onStop() {
        super.onStop();

        /*
            On unregister le listener sur le sensor s
         */
        sm.unregisterListener(audricListener,s);

        myListener.stop();
    }

}
