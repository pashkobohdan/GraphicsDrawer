package library.solver;

import library.function.RunnableDoubleFunction;

import java.util.List;

/**
 *
 */
public interface SolverForTwoFunctions {

    /**
     *
     *
     * @param firstFunction
     * @param secondFunction
     * @param from
     * @param to
     * @param searchStep
     * @param accuracy
     * @return
     */
    List<Double> solve(RunnableDoubleFunction firstFunction, RunnableDoubleFunction secondFunction, Double from, Double to, Double searchStep, Double accuracy);
}
