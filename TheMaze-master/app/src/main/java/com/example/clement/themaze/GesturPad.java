package com.example.clement.themaze;
import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;



public class GesturPad implements GestureDetector.OnGestureListener,  GestureDetector.OnDoubleTapListener, View.OnTouchListener {
    private long lastUpdate = 0;

    GestureDetectorCompat detector;
    TheMaze theMaze;
    float w;
    float h;

    public GesturPad(Context c, View source, TheMaze mazeView) {
        detector = new GestureDetectorCompat(c,this);
        detector.setOnDoubleTapListener(this);
        this.theMaze =mazeView;


    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {

        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {

        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return true;
    }


    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (! this.theMaze.mazeView.mapView) {
                    float x = motionEvent.getX();
                    float y = motionEvent.getY();
                    this.theMaze.mazeView.changeXYOnClick(x, y*2);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (! this.theMaze.mazeView.mapView) {

                    float x = motionEvent.getX();
                    float y = motionEvent.getY();
                    this.theMaze.mazeView.changeXYOnClick(x, y*2);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (! this.theMaze.mazeView.mapView) {

                    long curTime = System.currentTimeMillis();

                    if ((curTime - lastUpdate) > 50) {
                        long diffTime = (curTime - lastUpdate);
                        lastUpdate = curTime;
                        float x = motionEvent.getX();
                        float y = motionEvent.getY();
                        this.theMaze.mazeView.changeXYOnClick(x, y*2);
                    }
                }
            }
        }

        return true;

    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        theMaze.switchView();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }
}