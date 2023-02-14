package org.alongolov;
import org.junit.jupiter.api.Assertions;

class QuadraticSolverTest {
    @org.junit.jupiter.api.Test
    void solve0() {
        var s = new QuadraticSolver();
        var answer = s.solve(1,0,4);
        Assertions.assertEquals(0, answer.size());
    }

    @org.junit.jupiter.api.Test
    void solve1() {
        var s = new QuadraticSolver();
        var answer = s.solve(1,2,1);
        Assertions.assertEquals(1, answer.size());
        Assertions.assertEquals(-1, answer.get(0), 0.0001);
    }

    @org.junit.jupiter.api.Test
    void solve2() {
        var s = new QuadraticSolver();
        var answer = s.solve(1,0,-4);
        Assertions.assertEquals(2, answer.size());
        Assertions.assertEquals(-2, answer.get(0), 0.0001);
        Assertions.assertEquals(2, answer.get(1), 0.0001);
    }
}