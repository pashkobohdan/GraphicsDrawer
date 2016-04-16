package library.solver;

import javafx.geometry.Point2D;
import library.function.RunnableDoubleFunction;

import java.util.List;

/**
 * Solve two function (f(x) = g(x)), what set like RunnableDoubleFunction (implemented class)
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
    List<Point2D> solve(RunnableDoubleFunction firstFunction, RunnableDoubleFunction secondFunction, Double from, Double to, Double searchStep, Double accuracy);
}
