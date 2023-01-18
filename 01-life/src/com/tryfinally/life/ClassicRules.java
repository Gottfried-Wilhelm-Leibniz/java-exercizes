package com.tryfinally.life;
import java.util.ArrayList;

public class ClassicRules implements Rules<Boolean> {
    public final ArrayList<Integer> deatTolive;
    public final ArrayList<Integer> liveTolive;

    public ClassicRules() {
        this.deatTolive = new ArrayList<>(1);
        this.deatTolive.add(3);
        this.liveTolive = new ArrayList<>(2);
        this.liveTolive.add(2);
        this.liveTolive.add(3);
    }

    @Override
    public Boolean nextState(Grid<Boolean> currentGrid, int i, int j) {
        int neighbours = 0;
        neighbours = compuatation(neighbours, checkPoint(i-1,j-1, currentGrid));
        neighbours = compuatation(neighbours, checkPoint(i-1,j, currentGrid));
        neighbours = compuatation(neighbours, checkPoint(i-1,j+1, currentGrid));
        neighbours = compuatation(neighbours, checkPoint(i,j+1, currentGrid));
        neighbours = compuatation(neighbours, checkPoint(i+1,j+1, currentGrid));
        neighbours = compuatation(neighbours, checkPoint(i+1,j, currentGrid));
        neighbours = compuatation(neighbours, checkPoint(i+1,j-1, currentGrid));
        neighbours = compuatation(neighbours, checkPoint(i,j-1, currentGrid));

        if(currentGrid.get(i, j)) {
            if(liveTolive.contains(neighbours)) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            if(deatTolive.contains(neighbours)) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    int checkPoint(int w, int h, Grid<Boolean> currentGrid) {
        try {
            if (currentGrid.get(w,h)) {
                return 1;
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {

        }
        return 0;
    }
    public int compuatation(int a, int b) {
        return a+b;
    }
}
