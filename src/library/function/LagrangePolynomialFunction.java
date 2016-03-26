package library.function;

import javafx.geometry.Point2D;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LagrangePolynomialFunction implements RunnableDoubleFunction {
    private List<Point2D> dots;
    private List<Double> coefficients;

    public LagrangePolynomialFunction(List<Point2D> dots) {
        this.dots = dots;
    }

    public LagrangePolynomialFunction(Map<Double, Double> dotsMap) {
        dots = new LinkedList<>();

        for (Double key : dotsMap.keySet()) {
            dots.add(new Point2D(key, dotsMap.get(key)));
        }
    }

    @Override
    public Double functionRun(double arg) {
        return null;
    }

    @Override
    public boolean initialize() {
        if (dots == null || dots.size() == 0) {
            return false;
        }

        coefficients = new LinkedList<>();

        for (int index = 0; index < dots.size(); index++) {

        }


        return true;
    }
}
