package library.graphic.settings;

import javafx.scene.paint.Color;
import library.function.LagrangePolynomialFunction;
import library.function.PolynomialFunction;
import library.function.RunnableDoubleFunction;
import library.function.StringFunction;
import library.function.settings.FunctionSettings;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;

/**
 * Created by Bohdan Pashko on 04.04.2016.
 */
@XmlRootElement
public class WriteFunctionsSettings {
    private WriteFunction functionF;
    private WriteFunction functionG;

    public WriteFunctionsSettings() {

    }

    public static WriteFunctionsSettings getByFunctions(RunnableDoubleFunction functionF, RunnableDoubleFunction functionG) {
        WriteFunctionsSettings result = new WriteFunctionsSettings();
        if (functionF != null) {
            result.setFunctionF(new WriteFunction(functionF.getFunctionSettings().getGraphicColor().toString(), functionF.getType(), functionF.toString()));
        }
        if (functionG != null) {
            result.setFunctionG(new WriteFunction(functionG.getFunctionSettings().getGraphicColor().toString(), functionG.getType(), functionG.toString()));
        }

        return result;
    }

    public RunnableDoubleFunction getRunnableFunctionF() {
        return getFunction(functionF);
    }

    public RunnableDoubleFunction getRunnableFunctionG() {
        return getFunction(functionG);
    }


    private RunnableDoubleFunction getFunction(WriteFunction writeFunction) {
        RunnableDoubleFunction result;
        if (writeFunction == null) {
            return null;
        }
        switch (writeFunction.getTypeOfFunction()) {
            case 0:
                result = new StringFunction().parseByString(writeFunction.getFunctionString());
                result.setFunctionSettings(new FunctionSettings(Color.web(writeFunction.getFunctionColor())));
                break;
            case 1:
                result = new PolynomialFunction().parseByString(writeFunction.getFunctionString());
                result.setFunctionSettings(new FunctionSettings(Color.web(writeFunction.getFunctionColor())));
                break;
            case 2:
                result = new LagrangePolynomialFunction().parseByString(writeFunction.getFunctionString());
                result.setFunctionSettings(new FunctionSettings(Color.web(writeFunction.getFunctionColor())));
                break;
            default:
                result = null;
        }

        return result;
    }

    public static boolean write(String fileName, WriteFunctionsSettings writeFunctionsSettings) {
        try {
            File selectedFile = new File(fileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(WriteFunctionsSettings.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(writeFunctionsSettings, selectedFile);
            System.out.println("Write successful !");
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static WriteFunctionsSettings read(String fileName) {
        if (!new File(fileName).exists()) {
            return null;
        }
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(WriteFunctionsSettings.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            WriteFunctionsSettings customer = (WriteFunctionsSettings) jaxbUnmarshaller.unmarshal(new File(fileName));
            return customer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public WriteFunction getFunctionF() {
        return functionF;
    }

    public void setFunctionF(WriteFunction functionF) {
        this.functionF = functionF;
    }

    public WriteFunction getFunctionG() {
        return functionG;
    }

    public void setFunctionG(WriteFunction functionG) {
        this.functionG = functionG;
    }
}
