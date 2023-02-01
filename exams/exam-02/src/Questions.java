import java.util.*;

public class Questions {
    public static void main(String[] args) {

    }

    private static Object fintFirstDuplicate(List list) {
        var set = new HashSet();
        for (var item : list) {
            if (!set.add(item)) {
                return item;
            }
        }
        return null;
    }

    private static List ewavesLists(List a, List b) {
        var list = new ArrayList(a.size() + b.size());
        for (int i = 0; i < a.size(); i++) {
            list.add(a.get(i));
            list.add(b.get(i));
        }
        return list;
    }
}

//        var list2 = new LinkedList();
//        for (int i = 0; i < a.size(); i++) {
//            list.add(a.get(i));
//            list.add(b.get(i));
//        }
//return list2
//        var list3 = new ArrayList(a.size() + b.size());
//        var iterA = a.iterator();
//        var iterB = b.iterator();
//        while (iterA.hasNext()) {
//            list3.add(iterA.next());
//            if (iterB.hasNext()) {
//                list3.add(iterB.next());
//            }
//        }
//        return list3;