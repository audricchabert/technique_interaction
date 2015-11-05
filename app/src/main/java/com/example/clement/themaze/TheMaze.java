package com.example.clement.themaze;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_the_maze)
public class TheMaze extends Activity {

    @ViewById
    Button buttonSwitchView;

    @ViewById
    MazeView mazeView;

    @Click
    public void buttonSwitchView(){
        mazeView.setMapView();
        Toast.makeText(TheMaze.this, "TESTOUILLE", Toast.LENGTH_SHORT).show();
    }

    @AfterViews
    public void initButton(){
        buttonSwitchView.setText("Switch View");
    }

}
