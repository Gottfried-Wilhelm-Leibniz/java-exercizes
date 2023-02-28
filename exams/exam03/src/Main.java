import q1.InvalidArguments;

import javax.naming.OperationNotSupportedException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        //q1
//        var a = new int[]{2,4,3,1,5,3,3,4};
//        System.out.println("ANS: " + kGreatest(a, -2));
        //q2
//        Set<Integer> a = Set.of(2,3,4);
//        Set<Integer> b = Set.of(4,5,6);
//        System.out.println(sets(List.of(a,b)));
        // q3
        System.out.println(fibRec(50));
    }

    private static int kGreatest(int[] arr, int n) {
        var s = Arrays.stream(arr).sorted().distinct().toArray();
        try {
            return s[s.length - n];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidArguments("Operation is impossible");
        }
    }
    private static <T> Set<T> sets(List<Set<T>> list) {
        var n = new HashSet<T>();
        list.forEach(n::addAll);
        return n;
    }

    private static long fibRec(long index) {
        if(index == 1) {
            return 0;
        }
        if(index == 2) {
            return 1;
        }
        return fibRec(index - 1) + fibRec(index - 2);
    }
    private static <T> List<T> listDupRemoved(List<T> list) {
        var newList = new ArrayList<T>();

    }

}
