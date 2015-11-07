package com.example.clement.themaze;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
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

        Drawable d = getResources().getDrawable(R.drawable.chemin);
        if (mapView) {
            int largeurCase =Math.min( w / 16,h/16);
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
                            canvas.drawRect(y1,x1,y2,x2, paint);

                        }
                        else if ( maze.getGrille()[caseX - 2 + i][caseY - 2 + j] == 3){
                            paint.setColor(Color.RED);

                            canvas.drawRect(y1,x1,y2,x2, paint);
                        }
                        else if ( maze.getGrille()[caseX - 2 + i][caseY - 2 + j] == 0){
                            paint.setColor(Color.BLACK);
                            canvas.drawRect(y1,x1,y2,x2, paint);
                        }
                        else{
                            d.setBounds(y1,x1,y2,x2);
                            d.draw(canvas);
                        }
                    }
                    else{
                        paint.setColor(Color.BLACK);
                        canvas.drawRect(y1,x1,y2,x2, paint);
                    }
                }
            }
            canvas.drawCircle(tailleFenetre,tailleFenetre,largeurCase,paint);
        }
    }

    public void changeXY(float y , float x){
        int largeurCase =Math.min( w / 16,h/16);
        int newX=this.x;
        int newY=this.y;
        if (x>2||x<-2){
            newX+=x;
        }

        if (y>2|| y<-2){
            newY+=-y;
        }

        int caseX=newX/largeurCase;
        int caseY=newY/largeurCase;

        if (!(caseX < 0 || caseX >= maze.getX()) && !(caseY  < 0 || caseY >= maze.getY()) && !(maze.getGrille()[caseX][caseY] == 0)) {
            this.x=newX;
            this.y=newY;
        }
        caseX=this.x/largeurCase;
        caseY=newY/largeurCase;
        if (!(caseX < 0 || caseX >= maze.getX()) && !(caseY  < 0 || caseY >= maze.getY()) && maze.getGrille()[caseX][caseY] == 0) {
            this.x=newX;
        }
        caseX=newX/largeurCase;
        caseY=this.y/largeurCase;
        if (!(caseX < 0 || caseX >= maze.getX()) && !(caseY  < 0 || caseY >= maze.getY()) && maze.getGrille()[caseX][caseY] == 0) {
            this.y=newY;
        }

    }
   public void setMapView(){
       mapView=!mapView;
   }
}
