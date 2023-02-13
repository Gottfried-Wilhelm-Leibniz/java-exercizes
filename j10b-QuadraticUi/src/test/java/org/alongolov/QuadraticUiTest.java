package org.alongolov;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

class QuadraticUiTest {
    @org.junit.jupiter.api.Test
    void uiIt() {
        var ui = new QuadraticUi();
        var answer = ui.uiIt(1,0,4);
        Assertions.assertTrue(answer.size() == 0);
        answer = ui.uiIt(1,0,-4);
        Assertions.assertTrue(answer.size() == 2);
        Assertions.assertEquals(2, answer.get(0), 0.0001);
        Assertions.assertEquals(-2, answer.get(1), 0.0001);
        answer = ui.uiIt(1,2,1);
        Assertions.assertTrue(answer.size() == 1);
        Assertions.assertEquals(-1, answer.get(0), 0.0001);
    }

}

