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
            for(int j =0; j<16;j++) {
                    grille[i][j] = 1;
            }
        }
        grille[0][0]=2;
        grille[5][10]=3;
        grille[3][5]=4;
        grille[10][15]=5;
        grille[15][15]=6;
        grille[4][14]=7;
        grille[10][5]=8;
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
