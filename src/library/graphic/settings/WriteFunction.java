package library.graphic.settings;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by pashk on 04.04.2016.
 */
@XmlRootElement
public class WriteFunction {
    private String functionColor;
    private int typeOfFunction; // (0,1,2) - string, polynomial, lagrange
    private String functionString;

    public WriteFunction(){

    }

    public WriteFunction(String functionColor, int typeOfFunction, String functionString) {
        this.functionColor = functionColor;
        this.typeOfFunction = typeOfFunction;
        this.functionString = functionString;
    }

    public String getFunctionColor() {
        return functionColor;
    }

    public void setFunctionColor(String functionColor) {
        this.functionColor = functionColor;
    }

    public int getTypeOfFunction() {
        return typeOfFunction;
    }

    public void setTypeOfFunction(int typeOfFunction) {
        this.typeOfFunction = typeOfFunction;
    }

    public String getFunctionString() {
        return functionString;
    }

    public void setFunctionString(String functionString) {
        this.functionString = functionString;
    }
}
