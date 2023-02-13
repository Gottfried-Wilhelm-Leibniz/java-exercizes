package org.alongolov;
import org.junit.jupiter.api.Assertions;

class QuadraticSolverTest {
    @org.junit.jupiter.api.Test
    void solve() {
        var s = new QuadraticSolver();
        var answer = s.solve(1,0,4);
        Assertions.assertTrue(answer.size() == 0);
        answer = s.solve(1,0,-4);
        Assertions.assertTrue(answer.size() == 2);
        Assertions.assertEquals(2, answer.get(0));
        Assertions.assertEquals(-2, answer.get(1));
        answer = s.solve(1,2,1);
        Assertions.assertTrue(answer.size() == 1);
        Assertions.assertEquals(-1, answer.get(0));
    }
}