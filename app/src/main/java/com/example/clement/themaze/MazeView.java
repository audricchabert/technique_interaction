package com.example.clement.themaze;

import android.content.Context;
import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.util.AttributeSet;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
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
    private float angle;
    private long lastUpdate = 0;

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
        int size =Math.min(w, h);
        setMeasuredDimension(size, size);
        this.w=getMeasuredWidth();
        this.h=getMeasuredHeight();

    }

    @Override
    public void onDraw(Canvas canvas){


        Drawable d = getResources().getDrawable(R.drawable.chemin2);
        Drawable d2 = getResources().getDrawable(R.drawable.buisson);
        Drawable start = getResources().getDrawable(R.drawable.start);
        Drawable end = getResources().getDrawable(R.drawable.start2);
        Drawable character = getResources().getDrawable(R.drawable.character);
        if (mapView) {
            int largeurCase =Math.min( w / 16,h/16);
            paint.setColor(Color.BLACK);

            for (int i = 0; i < maze.getX(); i++) {
                for (int j = 0; j < maze.getY(); j++) {
                    if (maze.getGrille()[i][j] == 0) {
                        d2.setBounds(largeurCase * j, largeurCase * i, largeurCase * (j + 1), largeurCase * (i + 1));
                        d2.draw(canvas);

                        /*
                        paint.setColor(Color.BLACK);
                        canvas.drawRect(largeurCase*j,largeurCase*i,largeurCase*(j+1),largeurCase*(i+1),paint);*/
                    }
                    else if (maze.getGrille()[i][j] == 2) {
                        /*paint.setColor(Color.BLUE);
                        canvas.drawRect(largeurCase*j,largeurCase*i,largeurCase*(j+1),largeurCase*(i+1),paint);*/
                        start.setBounds(largeurCase * j, largeurCase * i, largeurCase * (j + 1), largeurCase * (i + 1));
                        start.draw(canvas);
                        if(init) {
                            x = i * largeurCase + largeurCase / 2;
                            y = j * largeurCase + largeurCase / 2;
                            init=false;
                        }
                    }
                    else if (maze.getGrille()[i][j] == 3) {
                        /*paint.setColor(Color.RED);
                        canvas.drawRect(largeurCase*j,largeurCase*i,largeurCase*(j+1),largeurCase*(i+1),paint);*/
                        end.setBounds(largeurCase * j, largeurCase * i, largeurCase * (j + 1), largeurCase * (i + 1));
                        end.draw(canvas);
                    }
                    else{
                        d.setBounds(largeurCase * j, largeurCase * i, largeurCase * (j + 1), largeurCase * (i + 1));
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
                            /*paint.setColor(Color.BLUE);
                            canvas.drawRect(y1, x1, y2, x2, paint);*/
                            start.setBounds(y1, x1, y2, x2);
                            start.draw(canvas);

                        }
                        else if ( maze.getGrille()[caseX - 2 + i][caseY - 2 + j] == 3){
                            /*paint.setColor(Color.RED);
                            canvas.drawRect(y1,x1,y2,x2, paint);*/
                            end.setBounds(y1, x1, y2, x2);
                            end.draw(canvas);
                        }
                        else if ( maze.getGrille()[caseX - 2 + i][caseY - 2 + j] == 0){
                            d2.setBounds(y1, x1, y2, x2);
                            d2.draw(canvas);
                        }
                        else{
                            d.setBounds(y1,x1,y2,x2);
                            d.draw(canvas);
                        }
                    }
                    else{
                        d2.setBounds(y1, x1, y2, x2);
                        d2.draw(canvas);
                    }
                }
            }
            /*Drawable layer=rotateDrawable(character,0);
            layer.setBounds(w / 2-largeurCase, h / 2-largeurCase, (w / 2) + largeurCase , (h / 2) + largeurCase);
            layer.draw(canvas);*/

            character.setBounds(w / 2-largeurCase, h / 2-largeurCase, (w / 2) + largeurCase , (h / 2) + largeurCase);
            character.draw(canvas);
            /*
            paint.setColor(Color.BLACK);
            canvas.drawCircle(tailleFenetre, tailleFenetre, largeurCase, paint);*/
        }
    }

    Drawable rotateDrawable(Drawable drawable, final float newangle) {
        Drawable[] arD = {
                drawable
        };
        return new LayerDrawable(arD) {
            @Override
            public void draw(Canvas newcanvas) {
                newcanvas.save();
                newcanvas.rotate(newangle);
                super.draw(newcanvas);
                newcanvas.restore();
            }
        };
    }

    public void changeXY(float y , float x){
        y=y*2;
        x=x*2;
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
        int caseX0=this.x/largeurCase;
        int caseY0=this.y/largeurCase;

        if (newX>=0 && newY>=0&&!(caseX < 0 || caseX >= maze.getX()) && !(caseY  < 0 || caseY >= maze.getY()) && !(maze.getGrille()[caseX][caseY] == 0)) {
            this.x=newX;
            this.y=newY;
            //this.angle=45;
        }
        else if (newY>=0 &&!(caseX0 < 0 || caseX0 >= maze.getX()) && !(caseY  < 0 || caseY >= maze.getY()) && !(maze.getGrille()[caseX0][caseY] == 0)) {
            this.y=newY;
            //this.angle=90;
        }
        else if (newX>=0 &&!(caseX < 0 || caseX >= maze.getX()) && !(caseY0  < 0 || caseY0 >= maze.getY()) &&!( maze.getGrille()[caseX][caseY0] == 0)) {
            this.x=newX;
            //this.angle=0;
        }
    }

    public void changeXYOnClick(float y,float x){
        int newX=(int) ((x - w/2)/16)+this.x;
        int newY=(int) ((y - h/2)/16)+this.y;
        int largeurCase =Math.min( w / 16,h/16);
        int caseX=newX/largeurCase;
        int caseY=newY/largeurCase;
        int caseX0=this.x/largeurCase;
        int caseY0=this.y/largeurCase;
        if (newX>=0 && newY>=0&&!(caseX < 0 || caseX >= maze.getX()) && !(caseY  < 0 || caseY >= maze.getY()) && !(maze.getGrille()[caseX][caseY] == 0)) {
            this.x=newX;
            this.y=newY;
            //this.angle=45;
        }
        else if (newY>=0 &&!(caseX0 < 0 || caseX0 >= maze.getX()) && !(caseY  < 0 || caseY >= maze.getY()) && !(maze.getGrille()[caseX0][caseY] == 0)) {
            this.y=newY;
            //this.angle=90;
        }
        else if (newX>=0 &&!(caseX < 0 || caseX >= maze.getX()) && !(caseY0  < 0 || caseY0 >= maze.getY()) &&!( maze.getGrille()[caseX][caseY0] == 0)) {
            this.x=newX;
            //this.angle=0;
        }
        Log.e("TAGLAUTRECAAAAAAaaCA", this.x +" "+this.y);
    }

   public void setMapView(){
        mapView=!mapView;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                float x= event.getX();
                float y= event.getY();
                changeXYOnClick(x, y);
                break;
            }
            case MotionEvent.ACTION_UP: {
                float x= event.getX();
                float y= event.getY();
                changeXYOnClick(x, y);
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                long curTime = System.currentTimeMillis();

                if ((curTime - lastUpdate) > 50) {
                    long diffTime = (curTime - lastUpdate);
                    lastUpdate = curTime;
                    float x = event.getX();
                    float y = event.getY();
                    changeXYOnClick(x, y);
                }
            }
        }
        return true;

    }
}
