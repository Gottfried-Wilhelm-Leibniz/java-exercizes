import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContainerXTest {
    @org.junit.jupiter.api.Test
    void push() {
        var a = Integer.valueOf(7);
        var b = Integer.valueOf(8);
        var c = Integer.valueOf(4);
        var d = Integer.valueOf(9);
        var e = Integer.valueOf(10);
        var cont = new ContainerX<Integer>();
        cont.push(c);
        cont.push(a);
        cont.push(b);
        cont.push(d);
        cont.push(e);
        Assertions.assertEquals(cont.min(), 4);
        cont.pop();
        Assertions.assertEquals(cont.min(), 4);
        cont.pop();
        Assertions.assertEquals(cont.min(), 4);
        cont.pop();
        Assertions.assertEquals(cont.min(), 4);

    }

    @Test
    void push_and_min_test() {
        var cont = new ContainerX<Integer>();
        cont.push(5);
        cont.push(7);
        cont.push(3);
        cont.push(4);
        cont.push(2);
        cont.push(9);
        assertEquals(2, cont.min());
        assertEquals(9, cont.pop());
        assertEquals(2, cont.min());
        assertEquals(2, cont.pop());
        assertEquals(3, cont.min());
        assertEquals(4, cont.pop());
        assertEquals(3, cont.pop());
        assertEquals(5, cont.min());
        assertEquals(7, cont.pop());
        assertEquals(5, cont.pop());
    }
//    @Test
//    void min_by_hight(){
//        var homer = new Person("Homer", 75, 1.75f);
//        var bart = new Person("Bart", 100, 1.42f);
//        var lisa = new Person("Lisa", 120, 1.39f);
//        var marge = new Person("Marge", 105, 1.79f);
//
//        stack.push(bart);
//        stack.push(marge);
//        stack.push(homer);
//        stack.push(lisa);
//
//        assertEquals(homer, stack.minimal());
//        assertEquals(lisa, stack.pop());
//        assertEquals(homer, stack.pop());
//
//        assertEquals(bart, stack.minimal());
//        assertEquals(marge, stack.pop());
//        assertEquals(bart, stack.minimal());
//    }


}