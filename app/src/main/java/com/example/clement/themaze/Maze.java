package com.example.clement.themaze;

/**
 * Created by clement on 13/10/2015.
 */
public class Maze {
    // x et y sont la tailles de la grille
    private int x;
    private int y;
    private int[][] grille;
    public Maze(int x, int y){
        this.x=x;
        this.y=y;
        grille=new int[x][y];
    }
    public Maze(){
        x=16;
        y=16;
        grille=new int[16][16];
        //0 noir, 1 blanc 2 entré 3 sortie
        for (int i =0;i<16;i++){
            if (i==7||i==8){
                grille[0][i]=1;
            }
            else
                grille[0][i]=0;
        }
        for (int i =0;i<16;i++){
            if (i==6||i==15||i==12){
                grille[1][i]=0;
                grille[2][i]=0;

            }
            else{
                grille[1][i]=1;
                grille[2][i]=1;
            }
        }
        for (int i =0;i<16;i++){
            if (i==4||i==5||i==10||i==11||i==13||i==14){
                grille[3][i]=1;
            }
            else
                grille[3][i]=0;
        }
        for (int i =0;i<16;i++){
            if (i==0||i==6||i==12||i==15){
                grille[4][i]=0;
                grille[5][i]=0;

            }
            else{
                grille[4][i]=1;
                grille[5][i]=1;
            }
        }
        for (int i =0;i<16;i++){
            if (i==1||i==2||i==8||i==7||i==13||i==14){
                grille[6][i]=1;
            }
            else
                grille[6][i]=0;
        }
        for (int i =0;i<16;i++){
            if (i==0||i==6||i==9||i==15){
                grille[7][i]=0;
                grille[8][i]=0;

            }
            else{
                grille[7][i]=1;
                grille[8][i]=1;
            }
        }
        for (int i =0;i<16;i++){
            if (i==0||i==3||i==6||i==9||i==12||i==15){
                grille[9][i]=0;
                grille[10][i]=0;
                grille[11][i]=0;
                grille[12][i]=0;
            }
            else{
                grille[9][i]=1;
                grille[10][i]=1;
                grille[11][i]=1;
                grille[12][i]=1;
            }
        }
        for (int i =0;i<16;i++){
            if (i==0||i==3||i==12||i==15){
                grille[13][i]=0;
                grille[14][i]=0;

            }
            else{
                grille[13][i]=1;
                grille[14][i]=1;
            }
        }
        for (int i =0;i<16;i++){
            grille[15][i]=0;
        }
        grille[1][0]= 2;
        grille[2][0]= 2;
        grille[0][7]= 3;
        grille[0][8]= 3;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int[][] getGrille() {
        return grille;
    }

    public void setGrille(int[][] grille) {
        this.grille = grille;
    }

}
