package library.function;

import javafx.geometry.Point2D;
import library.function.settings.FunctionSettings;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LagrangePolynomialScriptFunction implements RunnableDoubleFunction{
    private FunctionSettings functionSettings = new FunctionSettings();

    private List<Point2D> dots;
    private List<Double> coefficients;

    private String functionString;
    private ScriptEngine scriptEngine;
    private Invocable invocable;


    public LagrangePolynomialScriptFunction(Point2D... dots) {
        this.dots = Arrays.asList(dots);
    }

    public LagrangePolynomialScriptFunction(List<Point2D> dots) {
        this.dots = dots;
    }

    public LagrangePolynomialScriptFunction(Map<Double, Double> dotsMap) {
        dots = new LinkedList<>();

        for (Double key : dotsMap.keySet()) {
            dots.add(new Point2D(key, dotsMap.get(key)));
        }
    }


    @Override
    public Double functionRun(double arg) {
        try {
            return (double) invocable.invokeFunction("funcJS", arg);
        } catch (NoSuchMethodException e) {
            return null;
        } catch (ScriptException e) {
            return null;
        }
    }

    @Override
    public boolean initialize() {
        functionString = "function funcJS(x) {return ";

        initializeEngine();

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

        for (int index = 0; index < dots.size(); index++) {
            functionString+=coefficients.get(index)+"";

            for (int i = 0; i < dots.size(); i++) {
                if (i == index) {
                    continue;
                }
                functionString+=" * ( x - "+dots.get(i).getX()+" )";
            }
            functionString+="+";
        }

        functionString = functionString.substring(0,functionString.length()-1);

        functionString+= ";}";

        try {
            scriptEngine.eval(functionString);
            invocable = (Invocable) scriptEngine;
        } catch (ScriptException e) {
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
    public int getType() {
        return 2;
    }

    @Override
    public RunnableDoubleFunction parseByString(String string) {
        return null;
    }

    private void initializeEngine() {
        scriptEngine = new ScriptEngineManager().getEngineByName("js");
    }
}
