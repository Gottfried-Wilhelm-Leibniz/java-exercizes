package com.tryfinally.life;
import java.util.function.Supplier;
public class Grid<T> {
    private T[][] grid;

    public Grid(int width, int height, Supplier<T> predicate) {
        this.grid = (T[][]) new Object[width][height];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = predicate.get();
            }
        }
    }

    public T get(int w, int h) {
        return grid[w][h];
    }
    public void set(int w, int h, T put) {
        grid[w][h] = put;
    }
    public int getWidth() {
        return grid.length;
    }
    public int getHeight() {
        return grid[0].length;
    }
}
