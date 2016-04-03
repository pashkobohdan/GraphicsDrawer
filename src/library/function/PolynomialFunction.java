package library.function;

import library.function.settings.FunctionSettings;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PolynomialFunction implements RunnableDoubleFunction {
    private FunctionSettings functionSettings = new FunctionSettings();

    private List<Double> coefficients;

    public PolynomialFunction(List<Double> coefficients) {
        this.setCoefficients(coefficients);
    }

    public PolynomialFunction(Double... coefficients) {
        this.coefficients = Arrays.asList(coefficients);
    }

    public PolynomialFunction(Integer... coefficients) {
        this.coefficients = new LinkedList<>();
        for (double coefficient : coefficients) {
            this.coefficients.add(coefficient);
        }
    }

    @Override
    public Double functionRun(double arg) {
        double result = 0;

        for (int index = 0; index < coefficients.size(); index++) {
            result += coefficients.get(index) * Math.pow(arg, coefficients.size() - index - 1);
        }

        return result;
    }

    @Override
    public boolean initialize() {
        if (coefficients == null || coefficients.size() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public FunctionSettings getFunctionSettings() {
        return functionSettings;
    }

    @Override
    public void setFunctionSettings(FunctionSettings functionSettings) {
        this.functionSettings = functionSettings;
    }

    @Override
    public String toString(){
        return coefficients.toString();
    }

    public List<Double> getCoefficients() {
        return coefficients;
    }

    public void setCoefficients(List<Double> coefficients) {
        this.coefficients = coefficients;
    }
}
