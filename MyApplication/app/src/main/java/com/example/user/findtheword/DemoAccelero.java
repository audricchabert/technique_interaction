package com.example.user.findtheword;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;


public class DemoAccelero extends Activity {

    private MyAcceleroListener myListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_accelero);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        View v = findViewById(R.id.imageView);

        Drawable image = getResources().getDrawable(R.drawable.fleche2);
        int h = image.getIntrinsicHeight();
        int w = image.getIntrinsicWidth();

        myListener = new MyAcceleroListener(sm, v, d, w, h);
    }


    public void launchStop(View button) {
        /* listener du bouton, association faite dans le xml */
        if (myListener.isRunning()) myListener.stop();
        else myListener.start();
    }


    /* stop en cas de quit... */
    /* il faudra relancer l'écoute en cliquant sur le bouton */
    protected void onPause() {
        super.onPause();
        myListener.stop();
    }
    protected void onStop() {
        super.onStop();
        myListener.stop();
    }

}
