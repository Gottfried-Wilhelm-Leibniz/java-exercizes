package PackageQ2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ArrayNotReapetedGenericsTest {
    private final String arr[] = {"avi", "debi", "lol", "simcha", null, "shimi", "avi", "debi", "lol", "simcha"};

    @Test
    void answer() {
        var arrString = new ArrayNotReapetedGenerics<String>();
        String answer = arrString.answer(arr);
        Assertions.assertEquals("shimi", answer);
        var arr2 = Arrays.copyOf(arr, arr.length + 1);
        arr2[arr.length] = "shimi";
        try {
            answer = arrString.answer(arr2);
        } catch (NoUniqueObjectException a) {
            Assertions.assertEquals(a.getMessage(), "no such unique object");
        }

    }
}