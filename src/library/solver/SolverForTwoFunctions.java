package library.solver;

import library.function.RunnableDoubleFunction;

import java.util.List;

public interface SolverForTwoFunctions {
    List<Double> solve(RunnableDoubleFunction firstFunction, RunnableDoubleFunction secondFunction, Double from, Double to, Double searchStep, Double accuracy);
}
