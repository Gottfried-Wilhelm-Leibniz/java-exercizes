package PackageQ2;

public class ArrayNotReapetedGenerics<T> {
    public T answer(T[] arr) {
        int j;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) {
                continue;
            }
            for (j = 0; j < arr.length; j++) {
                if(arr[j] == null) {
                    continue;
                }
                if (i != j && arr[i].hashCode() == arr[j].hashCode()) {
                    break;
                }
                if (j == arr.length - 1) {
                    return arr[i];
                }
            }
        }
        throw new NoUniqueObjectException("no such unique object");
    }
}
