package com.example.clement.themaze;

import android.content.Context;
import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.util.AttributeSet;

import android.graphics.drawable.Drawable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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
    private boolean init= true;


    public MazeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mapView=true;
        paint=new Paint();
        this.setBackgroundColor(Color.WHITE);
        maze= new Maze();
        gameLoopThread = new GameLoopThread(this,context);
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
        int size =Math.min( w,h);

        setMeasuredDimension(size, size);
        this.w=getMeasuredWidth();
        this.h=getMeasuredHeight();

    }

    @Override
    public void onDraw(Canvas canvas){


        Drawable d = getResources().getDrawable(R.drawable.chemin2);
        Drawable d2 = getResources().getDrawable(R.drawable.buisson);
        if (mapView) {
            int largeurCase =Math.min( w / 16,h/16);
            paint.setColor(Color.BLACK);

            for (int i = 0; i < maze.getX(); i++) {
                for (int j = 0; j < maze.getY(); j++) {
                    if (maze.getGrille()[i][j] == 0) {
                        d2.setBounds(largeurCase*j,largeurCase*i,largeurCase*(j+1),largeurCase*(i+1));
                        d2.draw(canvas);
                        /*
                        paint.setColor(Color.BLACK);
                        canvas.drawRect(largeurCase*j,largeurCase*i,largeurCase*(j+1),largeurCase*(i+1),paint);*/
                    }
                    else if (maze.getGrille()[i][j] == 2) {
                        paint.setColor(Color.BLUE);
                        canvas.drawRect(largeurCase*j,largeurCase*i,largeurCase*(j+1),largeurCase*(i+1),paint);
                        if(init) {
                            x = i * largeurCase + largeurCase / 2;
                            y = j * largeurCase + largeurCase / 2;
                            init=false;
                        }
                    }
                    else if (maze.getGrille()[i][j] == 3) {
                        paint.setColor(Color.RED);
                        canvas.drawRect(largeurCase*j,largeurCase*i,largeurCase*(j+1),largeurCase*(i+1),paint);
                    }
                    else{
                        d.setBounds(largeurCase*j,largeurCase*i,largeurCase*(j+1),largeurCase*(i+1));
                        d.draw(canvas);
                    }
                }
            }
        }
        else {
            int largeurCase =Math.min( w / 16,h/16);
            int largeurCaseInNewFrame =Math.min( w/4,h/4);
            int tailleFenetre =Math.min( w / 2,h/2);
            int caseX=x/largeurCase;
            int caseY=y/largeurCase;
            int deCalageCaseX=x%largeurCase*largeurCaseInNewFrame/largeurCase;
            int deCalageCaseY=y%largeurCase*largeurCaseInNewFrame/largeurCase;

            paint.setColor(Color.BLACK);
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    int x1 =largeurCaseInNewFrame * i -deCalageCaseX;
                    int x2= largeurCaseInNewFrame * (i + 1)-deCalageCaseX;
                    int y1= largeurCaseInNewFrame * j -deCalageCaseY ;
                    int y2= largeurCaseInNewFrame * (j + 1)-deCalageCaseY;

                    if (!(caseX - 2 + i < 0 || caseX - 2 + i >= maze.getX()) && !(caseY - 2 + j < 0 || caseY - 2 + j >= maze.getY())) {

                        if ( maze.getGrille()[caseX - 2 + i][caseY - 2 + j] == 2){
                            paint.setColor(Color.BLUE);
                            canvas.drawRect(y1, x1, y2, x2, paint);

                        }
                        else if ( maze.getGrille()[caseX - 2 + i][caseY - 2 + j] == 3){
                            paint.setColor(Color.RED);

                            canvas.drawRect(y1,x1,y2,x2, paint);
                        }
                        else if ( maze.getGrille()[caseX - 2 + i][caseY - 2 + j] == 0){
                            d2.setBounds(y1,x1,y2,x2);
                            d2.draw(canvas);
                        }
                        else{
                            d.setBounds(y1,x1,y2,x2);
                            d.draw(canvas);
                        }
                    }
                    else{
                        d2.setBounds(y1,x1,y2,x2);
                        d2.draw(canvas);
                    }
                }
            }
            paint.setColor(Color.BLACK);
            canvas.drawCircle(tailleFenetre, tailleFenetre, largeurCase, paint);
        }
    }

    public void changeXY(float y , float x){
        if (x>2)this.x+=x;
        else if (x<-2)this.x+=x;
        if (y>2)this.y+=-y;
        else if (y<-2)this.y+=-y;
    }
   public void setMapView(){
       mapView=!mapView;
   }
}
