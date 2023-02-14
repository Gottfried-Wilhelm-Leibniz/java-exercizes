package org.alongolov;
import java.util.*;
import java.lang.Math;

public class QuadraticUi {
    private final QuadraticSolver solver = new QuadraticSolver();
    private final Comparator<Double> comparator = Comparator.naturalOrder();
    private final List<Double> parmas = new ArrayList<>((Arrays.asList(0.0,0.0,0.0)));
    private List<Double> answer;
    public void claculate() {
        try(Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter a: ");
            parmas.set(0, scanner.nextDouble());
            System.out.print("Enter b: ");
            parmas.set(1, scanner.nextDouble());
            System.out.print("Enter c: ");
            parmas.set(2, scanner.nextDouble());
            answer = solver.solve(parmas.get(0), parmas.get(1), parmas.get(2));
            answer = answer.stream().sorted(comparator).toList();
        }
    }
    @Override
    public String toString() {
        var epsilon = 0.000001d;
        var sb = new StringBuilder();
        for(int i = 0; i < parmas.size(); i++) {
            if(Math.abs(parmas.get(i)) < epsilon) {
                continue;
            }
            sb.append(switch ((int)Math.signum(parmas.get(i))) {
                case -1 -> "-";
                case 1 -> "+";
                default -> "";
            });
            if(!(Math.abs(parmas.get(i)) < epsilon + 1)) {
                sb.append(Math.abs(parmas.get(i)));
            }
            sb.append(switch (i) {
                case 0 -> "X^2";
                case 1 -> "X";
                default -> "";
            });
            sb.append(" ");
        }
        return sb.toString();
    }

    public String getRoots() {
        if(answer.size() == 0) {
            return "There are non real roots";
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < answer.size(); i++) {
            sb.append("Root #");
            sb.append(i + 1);
            sb.append(": ");
            sb.append(answer.get(i));
            sb.append(" ");
        }
        return sb.toString();
    }
}
