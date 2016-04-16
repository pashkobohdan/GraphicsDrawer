package library.solver;

import javafx.geometry.Point2D;
import library.function.RunnableDoubleFunction;

import java.util.LinkedList;
import java.util.List;

public class EqualTwoDoubleFunction implements SolverForTwoFunctions {

    private boolean equalityOfSign(double first, double second) {
        if (first * second < 0) {
            return false;
        }
        return true;
    }

    private double dichotomySolve(RunnableDoubleFunction firstFunction, RunnableDoubleFunction secondFunction, double from, double to, double accuracy) {
        double center = (from + to) / 2;
        while (Math.abs(from - to) > accuracy) {
            if (!equalityOfSign(firstFunction.functionRun(from) - secondFunction.functionRun(from),
                    firstFunction.functionRun(center) - secondFunction.functionRun(center))) {
                to = center;
            } else {
                from = center;
            }
            center = (from + to) / 2;
        }
        return Math.abs(center) < accuracy ? 0 : center;
    }

    @Override
    public List<Point2D> solve(RunnableDoubleFunction firstFunction, RunnableDoubleFunction secondFunction, Double from, Double to, Double searchStep, Double accuracy) {
        List<Point2D> resultList = new LinkedList<>();

        Double firstFunctionY = firstFunction.functionRun(from);
        Double secondFunctionY = secondFunction.functionRun(from);
        Double argFirstFunctionY, argSecondFunctionY;

        double root;
        for (double x = from + searchStep; x <= to; x += searchStep) {
            argFirstFunctionY = firstFunction.functionRun(x);
            argSecondFunctionY = secondFunction.functionRun(x);

            if (!equalityOfSign(firstFunctionY - secondFunctionY, argFirstFunctionY - argSecondFunctionY)) {
                root = dichotomySolve(firstFunction, secondFunction, x - searchStep, x, accuracy);
                resultList.add(new Point2D(root, firstFunction.functionRun(root)));
            }

            firstFunctionY = argFirstFunctionY;
            secondFunctionY = argSecondFunctionY;
        }

        return resultList;
    }
}
