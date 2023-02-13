package org.alongolov;
import java.util.List;
import java.util.Scanner;

public class QuadraticUi {
    private final QuadraticSolver solver = new QuadraticSolver();
    public void uiIt() {
        try(Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter a: ");
            var a = scanner.nextDouble();

            System.out.print("Enter b: ");
            var b = scanner.nextDouble();

            System.out.print("Enter c: ");
            var c = scanner.nextDouble();

            var answer = solver.solve(a, b, c);
            System.out.printf("%.2fx^2 + %.2fx + %.2f \n", a, b, c);
            System.out.println(answer);
        }
    } public List<Double> uiIt(double a, double b, double c) {
            return solver.solve(a, b, c);
    }
}
