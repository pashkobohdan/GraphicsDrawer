package library.function;

import library.function.settings.FunctionSettings;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class StringFunction implements RunnableDoubleFunction {
    private FunctionSettings functionSettings = new FunctionSettings();

    private String equationString;
    private String functionString;
    private ScriptEngineManager scriptEngineManager;
    private ScriptEngine scriptEngine;
    private Invocable invocable;

    public StringFunction() {
    }

    public StringFunction(String equationString) {
        this.equationString = equationString;
        equationString = convertFunction(equationString);
        functionString = "function funcJS(x) {return " + equationString + ";}";
    }

    private void initializeEngine() {
        scriptEngineManager = new ScriptEngineManager();
        scriptEngine = scriptEngineManager.getEngineByName("js");
    }

    private String convertFunction(String function) {
        String result = (" " + function)
                .toLowerCase()
                .replaceAll("\\+", " \\+  ")
                .replaceAll("-", " -  ")
                .replaceAll("\\*", " \\* ")
                .replaceAll("/", " /  ")
                .replaceAll("\\(", "\\(  ")
                .replaceAll(" pi", "Math.PI")
                .replaceAll(" p", "Math.PI")
                .replaceAll(" e", "Math.E")
                .replaceAll(" exp", "Math.E")
                .replaceAll(" sqrt\\(", "Math.sqrt\\(")
                .replaceAll(" sin\\(", "Math.sin\\(")
                .replaceAll(" cos\\(", "Math.cos\\(")
                .replaceAll(" tan\\(", "Math.tan\\(")
                .replaceAll(" ctg\\(", "1.0/Math.tan\\(")
                .replaceAll(" ctan\\(", "1.0/Math.tan\\(")
                .replaceAll(" tg\\(", "Math.tan\\(")
                .replaceAll(" ln\\(", "Math.log\\(")
                .replaceAll(" log\\(", "(1/Math.LN10)*Math.log\\(")
                .replaceAll(" lg\\(", "(1/Math.LN10)*Math.log\\(")
                .replaceAll(" arccos\\(", "Math.acos\\(")
                .replaceAll(" arcsin\\(", "Math.asin\\(")
                .replaceAll(" arctg\\(", "Math.atan\\(")
                .replaceAll(" arctan\\(", "Math.atan\\(")
                .replaceAll(" abs\\(", "Math.abs\\(");
        while (result.contains(" ")) {
            result = result.replaceAll(" ", "");
        }

        while (result.contains("^")) {
            result = searchPow(result);
        }

        return result;
    }

    private String searchPow(String function) {
        String result = "" + function;

        int powPosition = result.indexOf("^");
        int firstBegin = 0, lastBegin = -1;
        int firstEnd = -1, lastEnd = -1;
        String first = "", last = "";

        if (result.charAt(powPosition - 1) == ')') {
            int countSk = 1;
            int position = powPosition - 1;
            firstEnd = powPosition - 1;
            while (position > 0 && countSk != 0) {
                first = result.charAt(position) + first;
                position--;
                if (result.charAt(position) == ')') {
                    countSk++;
                }
                if (result.charAt(position) == '(') {
                    countSk--;
                }
            }
            first = result.charAt(position) + first;

            firstBegin = position;
        } else {
            int countSk = 1;
            int position = powPosition - 1;

            while (position >= 0 && countSk != 0) {
                if (result.charAt(position) == '+'
                        || result.charAt(position) == '-'
                        || result.charAt(position) == '('
                        || result.charAt(position) == '*'
                        || result.charAt(position) == '/') {
                    break;
                } else {
                    first = result.charAt(position) + first;
                    firstBegin = position;
                    position--;
                }
            }
        }

        if (result.charAt(powPosition + 1) == '(') {
            int countSk = 1;
            int position = powPosition + 1;
            lastBegin = powPosition + 1;
            while (position < result.length() - 1 && countSk != 0) {
                last = last + result.charAt(position);
                position++;
                if (result.charAt(position) == '(') {
                    countSk++;
                }
                if (result.charAt(position) == ')') {
                    countSk--;
                }
            }
            last = last + result.charAt(position);
            lastEnd = position;
        } else {
            int countSk = 1;
            int position = powPosition + 1;

            while (position < result.length() && countSk != 0) {
                if (result.charAt(position) == '+'
                        || result.charAt(position) == '-'
                        || result.charAt(position) == '('
                        || result.charAt(position) == '*'
                        || result.charAt(position) == '/'
                        || result.charAt(position) == ')') {
                    break;
                } else {
                    last = last + result.charAt(position);
                    System.out.println(last);
                    lastEnd = position;
                    position++;
                }
            }
        }

        result = result.substring(0, firstBegin) + "Math.pow(" + first + "," + last + ")" + result.substring(lastEnd + 1, result.length());

        return result;
    }

    @Override
    public Double functionRun(double arg) {
        try {
            return (double) invocable.invokeFunction("funcJS", arg);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean initialize() {
        if (equationString == null || equationString.length() == 0) {
            return false;
        }

        initializeEngine();

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
        return 0;
    }

    @Override
    public RunnableDoubleFunction parseByString(String string) {
        return new StringFunction(string);
    }

    @Override
    public String toString() {
        return equationString;
    }


}
