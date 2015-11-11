package com.example.user.findtheword;

import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private PhotoViewAttacher mAttacher;

    Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mAttacher = new PhotoViewAttacher(mImageView);

        chronometer = (Chronometer) findViewById(R.id.chronometer);



        //L'utilisation d'un scale n'est pas obligatoire
        //mAttacher.setMaximumScale(30.0f);
        //mAttacher.setScale(30.0f);

        //mAttacher.setOnDoubleTapListener();


        /*
            Ici le 2eme et 3eme argument sont le x et le y de la position
         */
        mAttacher.setScale(mAttacher.getMaximumScale(), mImageView.getX(), mImageView.getY(), true);

        //code pour le clic du bouton "lancer"
        Button button =(Button) findViewById(R.id.button_launch);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chronometer.start();

            }
        });


        button = (Button) findViewById(R.id.button_change);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                chronometer.setBase(SystemClock.elapsedRealtime());
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
