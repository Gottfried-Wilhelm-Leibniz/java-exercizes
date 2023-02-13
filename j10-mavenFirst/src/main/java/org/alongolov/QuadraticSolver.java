package org.alongolov;
import java.util.ArrayList;
import java.util.List;

public class QuadraticSolver {
    public List<Double> solve(double a, double b, double c) {
        var discriminant = b * b - 4 * a * c;
        List<Double> list = new ArrayList<>();
//        double[] arr;

        switch ((int) Math.signum(discriminant)) {
            case 1 -> {
//                arr = new double[2];
//                arr[0] = (- b + Math.sqrt(discriminant)) / (2 * a);
//                arr[1] = (- b - Math.sqrt(discriminant)) / (2 * a);
                list.add((- b + Math.sqrt(discriminant)) / (2 * a));
                list.add((- b - Math.sqrt(discriminant)) / (2 * a));
            }
            case 0 -> {
//                arr = new double[1];
                list.add(- b / (2 * a));
//                arr[0] = - b / (2 * a);
            }
//            case -1 -> {
//                arr = new double[0];
//            }
        }
        return list;
    }
}
