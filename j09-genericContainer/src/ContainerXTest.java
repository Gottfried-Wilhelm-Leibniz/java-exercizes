import org.junit.jupiter.api.Assertions;

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

}