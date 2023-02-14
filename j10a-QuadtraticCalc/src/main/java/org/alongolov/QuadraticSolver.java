package org.alongolov;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class QuadraticSolver {
    private final Comparator<Double> comparator = Comparator.naturalOrder();
    public List<Double> solve(double a, double b, double c) {
        var discriminant = b * b - 4 * a * c;
        List<Double> list = new ArrayList<>();
        switch ((int) Math.signum(discriminant)) {
            case 1 -> {
                list.add((- b - Math.sqrt(discriminant)) / (2 * a));
                list.add((- b + Math.sqrt(discriminant)) / (2 * a));
            }
            case 0 -> list.add(- b / (2 * a));
        }
        list = list.stream().sorted(comparator).toList();
        return list;
    }
}
