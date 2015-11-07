package com.example.clement.themaze;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;

public class GameLoopThread extends Thread {
    static final long FPS = 10;
    private MazeView view;
    private boolean running = false;

    public GameLoopThread(MazeView view, Context context) {
        this.view = view;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @Override
    public void run() {

        while (running) {
            Canvas c = null;
            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    Activity activity = (Activity) view.getContext();
                    activity.runOnUiThread(new Runnable(){
                        public void run(){
                            view.invalidate();

                        }
                    });
                }
            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }
            try {

                    sleep(10);
            } catch (Exception e) {}
        }
    }
}