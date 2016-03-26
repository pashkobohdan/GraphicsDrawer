package library.solver;

import library.function.RunnableDoubleFunction;

import java.util.LinkedList;
import java.util.List;

public class EqualTwoFunction implements SolverForTwoFunctions{

    private static boolean equalityOfSign(double first, double second) {
        if (first * second < 0) {
            return false;
        }
        return true;
    }

    private static double dichotomySolve(RunnableDoubleFunction firstFunction, RunnableDoubleFunction secondFunction, double from, double to, double accuracy) {
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
        return center;
    }

    @Override
    public List<Double> solve(RunnableDoubleFunction firstFunction, RunnableDoubleFunction secondFunction, Double from, Double to, Double searchStep, Double accuracy) {
        List<Double> resultList = new LinkedList<>();

        Double firstFunctionY = firstFunction.functionRun(from);
        Double secondFunctionY = secondFunction.functionRun(from);
        Double argFirstFunctionY, argSecondFunctionY;

        for (double x = from + searchStep; x <= to; x += searchStep) {
            argFirstFunctionY = firstFunction.functionRun(x);
            argSecondFunctionY = secondFunction.functionRun(x);

            if (!equalityOfSign(firstFunctionY - secondFunctionY, argFirstFunctionY - argSecondFunctionY)) {
                resultList.add(dichotomySolve(firstFunction, secondFunction, x - searchStep, x, accuracy));
            }

            firstFunctionY = argFirstFunctionY;
            secondFunctionY = argSecondFunctionY;
        }

        return resultList;
    }
}
