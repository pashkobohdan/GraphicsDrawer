package library.function;

import javafx.geometry.Point2D;
import library.function.settings.FunctionSettings;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LagrangePolynomialFunction implements RunnableDoubleFunction {
    private FunctionSettings functionSettings = new FunctionSettings();

    private List<Point2D> dots;
    private List<Double> coefficients;

    public LagrangePolynomialFunction() {

    }

    public LagrangePolynomialFunction(Point2D... dots) {
        this.dots = Arrays.asList(dots);
    }

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
    public boolean initialize() {
        if (dots == null || dots.size() == 0) {
            return false;
        }

        coefficients = new LinkedList<>();

        double multiCoefficient;
        double xI;
        for (int index = 0; index < dots.size(); index++) {
            multiCoefficient = dots.get(index).getY();
            xI = dots.get(index).getX();
            for (int i = 0; i < dots.size(); i++) {
                if (i == index) {
                    continue;
                }
                multiCoefficient /= (xI - dots.get(i).getX());
            }
            coefficients.add(multiCoefficient);
        }

        return true;
    }

    @Override
    public Double functionRun(double arg) {
        double result = 0;
        double multi;
        for (int index = 0; index < dots.size(); index++) {
            multi = coefficients.get(index);
            for (int i = 0; i < dots.size(); i++) {
                if (i == index) {
                    continue;
                }
                multi *= (arg - dots.get(i).getX());
            }
            result += multi;
        }

        return result;
    }

    @Override
    public String toString() {
        String result = "";
        for (Point2D point2D : dots) {
            result += point2D.getX() + ":" + point2D.getY() + " ";
        }
        return result;
    }

    @Override
    public void setFunctionSettings(FunctionSettings functionSettings) {
        this.functionSettings = functionSettings;
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public RunnableDoubleFunction parseByString(String string) {
        List<Point2D> coefficients = new LinkedList<>();
        String[] values;
        for (String coeff : string.split(" ")) {
            if (coeff.equals(" ")) {
                break;
            }
            values = coeff.split(":");
            coefficients.add(new Point2D(Double.parseDouble(values[0]), Double.parseDouble(values[1])));
        }
        return new LagrangePolynomialFunction(coefficients);
    }

    @Override
    public FunctionSettings getFunctionSettings() {
        return functionSettings;
    }
}
