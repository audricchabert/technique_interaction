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
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
        while (running) {
            Canvas c = null;
            startTime = System.currentTimeMillis();
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
            sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            } catch (Exception e) {}
        }
    }
}