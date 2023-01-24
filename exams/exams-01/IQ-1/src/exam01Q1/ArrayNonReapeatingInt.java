package exam01Q1;

public class ArrayNonReapeatingInt {

    public int answer(int[] arr) {
        int j;
        for (int i = 0; i < arr.length; i++) {
            for (j = 0; j < arr.length; j++) {
                if (i != j && arr[i] == arr[j]) {
                    break;
                }
                if (j == arr.length - 1) {
                    return arr[i];
                }
            }
        }
        throw new NosuchUniqueIntException("no such unique int");
    }
}
