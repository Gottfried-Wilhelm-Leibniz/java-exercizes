package com.tryfinally.life;
public interface Rules<T> {
    T nextState(Grid<T> grid, int i, int j);
}
