package controllers;

import controllers.choiceMethodsControllers.LagrangePolynomialFunctionController;
import controllers.choiceMethodsControllers.PolynomialFunctionController;
import controllers.choiceMethodsControllers.StringFunctionController;
import controllers.settings.GraphicSettingsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import library.function.RunnableDoubleFunction;
import library.function.settings.FunctionSettings;
import library.util.FXHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Bohdan Pashko on 26.03.2016.
 */
public class ChoiceFunctionSetController implements Initializable {
    @FXML
    public Label labelTitle, labelStatus, labelColor;

    @FXML
    public ComboBox comboBoxColor;

    @FXML
    private Button buttonSetString, buttonSetPolynomial, buttonSetLagrangePolynomial, buttonOk, buttonCancel;


    private RunnableDoubleFunction runnableDoubleFunction;
    private ResourceBundle resourceBundle;

    private FXMLLoader fxmlLoaderString;
    private Parent parentString;
    private StringFunctionController stringFunctionController;
    private Stage stringStage;

    private FXMLLoader fxmlLoaderPolynomial;
    private Parent parentPolynomial;
    private PolynomialFunctionController polynomialFunctionController;
    private Stage polynomialStage;

    private FXMLLoader fxmlLoaderLagrange;
    private Parent parentLagrange;
    private LagrangePolynomialFunctionController controller;
    private Stage lagrangeStage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;
        initializeWindows();

        labelColor.setText(resources.getString("function.color"));
        comboBoxColor.setItems(GraphicSettingsController.colors);
        GraphicSettingsController.setCurrentColor(Color.web(GraphicSettingsController.colors.get(0)), comboBoxColor, GraphicSettingsController.colors);

    }

    private boolean initializeWindows() {
        try {
            fxmlLoaderString = new FXMLLoader();
            fxmlLoaderString.setLocation(getClass().getResource("../fxml/choiceMethods/stringFunctionSet.fxml"));
            fxmlLoaderString.setResources(resourceBundle);
            parentString = fxmlLoaderString.load();
            stringFunctionController = fxmlLoaderString.getController();

            fxmlLoaderPolynomial = new FXMLLoader();
            fxmlLoaderPolynomial.setLocation(getClass().getResource("../fxml/choiceMethods/polynomialFunctionSet.fxml"));
            fxmlLoaderPolynomial.setResources(resourceBundle);
            parentPolynomial = fxmlLoaderPolynomial.load();
            polynomialFunctionController = fxmlLoaderPolynomial.getController();

            fxmlLoaderLagrange = new FXMLLoader();
            fxmlLoaderLagrange.setLocation(getClass().getResource("../fxml/choiceMethods/lagrangeFunctionSet.fxml"));
            fxmlLoaderLagrange.setResources(resourceBundle);
            parentLagrange = fxmlLoaderLagrange.load();
            controller = fxmlLoaderLagrange.getController();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void action_buttonSetString(ActionEvent actionEvent) {
        if (stringStage == null) {
            stringStage = FXHelper.initializeStage("", 300, 200, false, parentString, Modality.WINDOW_MODAL, labelTitle.getScene().getWindow());
        }

        stringStage.showAndWait();

        if (stringFunctionController.getRunnableDoubleFunction() != null) {
            runnableDoubleFunction = stringFunctionController.getRunnableDoubleFunction();
            labelStatus.setText("function was set in string ");
        }
    }

    public void action_buttonSetPolynomial(ActionEvent actionEvent) {
        if (polynomialStage == null) {
            polynomialStage = FXHelper.initializeStage("", 300, 200, false, parentPolynomial, Modality.WINDOW_MODAL, labelTitle.getScene().getWindow());
        }
        polynomialStage.showAndWait();

        if (polynomialFunctionController.getRunnableDoubleFunction() != null) {
            runnableDoubleFunction = polynomialFunctionController.getRunnableDoubleFunction();
            labelStatus.setText("function was set in polynomial form ");
        }
    }

    public void action_buttonSetLagrangePolynomial(ActionEvent actionEvent) {
        if (lagrangeStage == null) {
            lagrangeStage = FXHelper.initializeStage("", 300, 200, false, parentLagrange, Modality.WINDOW_MODAL, labelTitle.getScene().getWindow());
        }
        lagrangeStage.showAndWait();

        if (controller.getRunnableDoubleFunction() != null) {
            runnableDoubleFunction = controller.getRunnableDoubleFunction();
            labelStatus.setText("function was set in polynomial form ");
        }
    }

    public void action_buttonOk(ActionEvent actionEvent) {
        if(runnableDoubleFunction != null){
            runnableDoubleFunction.setFunctionSettings(new FunctionSettings(Color.web((String) comboBoxColor.getSelectionModel().getSelectedItem())));
        }
        labelStatus.getScene().getWindow().hide();
    }

    public void action_buttonCancel(ActionEvent actionEvent) {
        runnableDoubleFunction = null;
        labelStatus.getScene().getWindow().hide();
    }

    public RunnableDoubleFunction getRunnableDoubleFunction() {
        return runnableDoubleFunction;
    }

    public void setRunnableDoubleFunction(RunnableDoubleFunction runnableDoubleFunction) {
        this.runnableDoubleFunction = runnableDoubleFunction;
        GraphicSettingsController.setCurrentColor(runnableDoubleFunction==null?Color.BLACK:runnableDoubleFunction.getFunctionSettings().getGraphicColor(), comboBoxColor, GraphicSettingsController.colors);
    }
}
