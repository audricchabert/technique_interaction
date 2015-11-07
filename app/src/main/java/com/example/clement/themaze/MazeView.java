package com.example.clement.themaze;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.R;
import java.io.File;
import java.io.InputStream;
import java.net.URL;


public class MazeView extends SurfaceView {
    Paint paint;
    Maze maze;
    private int w;
    private int h;
    private GameLoopThread gameLoopThread;
    private SurfaceHolder holder;
    private Bitmap bmp;
    private boolean mapView;
    private int x;
    private int y;


    public MazeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mapView=true;
        paint=new Paint();
        this.setBackgroundColor(Color.WHITE);
        maze= new Maze();
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });

    }
    @Override
    protected void onMeasure(int w, int h){
        setMeasuredDimension(w, h);
        this.w=getMeasuredWidth();
        this.h=getMeasuredHeight();
    }

    @Override
    public void onDraw(Canvas canvas){
        /*try ( InputStream is = new URL( file_url ).openStream() ) {
            Bitmap bitmap = BitmapFactory.decodeStream( is );
        }*/
        InputStream resource = getResources().openRawResource(R.drawable.background_maze);
        Bitmap bitmap = BitmapFactory.decodeStream(resource);

        //canvas.drawBitmap(background, 15,15,this.paint);
        if (mapView) {
            int largeurCase =Math.min( w / 16,h/16);
            //paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setColor(Color.BLACK);

            for (int i = 0; i < maze.getX(); i++) {
                for (int j = 0; j < maze.getY(); j++) {
                    if (maze.getGrille()[i][j] == 0) {
                        paint.setColor(Color.BLACK);
                        canvas.drawRect(largeurCase*j,largeurCase*i,largeurCase*(j+1),largeurCase*(i+1),paint);
                    }
                    else if (maze.getGrille()[i][j] == 2) {
                        paint.setColor(Color.BLUE);
                        canvas.drawRect(largeurCase*j,largeurCase*i,largeurCase*(j+1),largeurCase*(i+1),paint);
                        x=i*largeurCase+largeurCase/2;
                        y=j*largeurCase+largeurCase/2;
                    }
                    else if (maze.getGrille()[i][j] == 3) {
                        paint.setColor(Color.RED);
                        canvas.drawRect(largeurCase*j,largeurCase*i,largeurCase*(j+1),largeurCase*(i+1),paint);
                    }
                }
            }
        }
        else {
            int largeurCase =Math.min( w / 16,h/16);
            paint.setColor(Color.BLACK);
            canvas.drawCircle(x,y,largeurCase/4,paint);

        }
    }
   public void setMapView(){
       mapView=!mapView;
   }
}
