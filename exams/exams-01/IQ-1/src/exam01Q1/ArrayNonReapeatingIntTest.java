package exam01Q1;
import org.junit.jupiter.api.*;

import java.util.Arrays;

class ArrayNonReapeatingIntTest {
    private final int arr[] = {1,2,3,4,5,6,6,3,2,1};

    @Test
    void answer() {
        var arrayInt = new ArrayNonReapeatingInt();
        int answer = arrayInt.answer(arr);
        Assertions.assertTrue(answer == 4);
        var arr2 = Arrays.copyOf(arr, arr.length + 1);
        arr2[arr.length] = 4;
        answer = arrayInt.answer(arr2);
        Assertions.assertTrue(answer == 5);
        var arr3 = Arrays.copyOf(arr2, arr2.length + 1);
        arr3[arr2.length] = 5;
        try {
            answer = arrayInt.answer(arr3);
        } catch (NosuchUniqueIntException a) {
            Assertions.assertEquals(a.getMessage(), "no such unique int");
        }
    }
}