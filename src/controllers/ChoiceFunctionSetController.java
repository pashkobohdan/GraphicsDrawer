package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import library.function.RunnableDoubleFunction;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Bohdan Pashko on 26.03.2016.
 */
public class ChoiceFunctionSetController implements Initializable {
    @FXML
    public Label labelTitle, labelStatus;

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
    }

    private boolean initializeWindows() {
        try {
            fxmlLoaderString = new FXMLLoader();
            fxmlLoaderString.setLocation(getClass().getResource("../fxml/stringFunctionSet.fxml"));
            fxmlLoaderString.setResources(resourceBundle);
            parentString = fxmlLoaderString.load();
            stringFunctionController = fxmlLoaderString.getController();

            fxmlLoaderPolynomial = new FXMLLoader();
            fxmlLoaderPolynomial.setLocation(getClass().getResource("../fxml/polynomialFunctionSet.fxml"));
            fxmlLoaderPolynomial.setResources(resourceBundle);
            parentPolynomial = fxmlLoaderPolynomial.load();
            polynomialFunctionController = fxmlLoaderPolynomial.getController();

            fxmlLoaderLagrange = new FXMLLoader();
            fxmlLoaderLagrange.setLocation(getClass().getResource("../fxml/lagrangeFunctionSet.fxml"));
            fxmlLoaderLagrange.setResources(resourceBundle);
            parentLagrange = fxmlLoaderLagrange.load();
            controller = fxmlLoaderLagrange.getController();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private Stage initializeStage(String title, int minWidth, int minHeight, boolean resizable, Parent parent, Modality modality, Window owner) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setMinWidth(minWidth);
        stage.setMinHeight(minHeight);
        stage.setResizable(resizable);
        stage.setScene(new Scene(parent));
        stage.initModality(modality);
        stage.initOwner(owner);
        return stage;
    }

    public void action_buttonSetString(ActionEvent actionEvent) {
        if (stringStage == null) {
            stringStage = initializeStage("", 300, 200, false, parentString, Modality.WINDOW_MODAL, labelTitle.getScene().getWindow());
        }

        stringStage.showAndWait();

        if (stringFunctionController.getRunnableDoubleFunction() != null) {
            runnableDoubleFunction = stringFunctionController.getRunnableDoubleFunction();
            labelStatus.setText("function was set in string ");
        }
    }

    public void action_buttonSetPolynomial(ActionEvent actionEvent) {
        if (polynomialStage == null) {
            polynomialStage = initializeStage("", 300, 200, false, parentPolynomial, Modality.WINDOW_MODAL, labelTitle.getScene().getWindow());
        }
        polynomialStage.showAndWait();

        if (polynomialFunctionController.getRunnableDoubleFunction() != null) {
            runnableDoubleFunction = polynomialFunctionController.getRunnableDoubleFunction();
            labelStatus.setText("function was set in polynomial form ");
        }
    }

    public void action_buttonSetLagrangePolynomial(ActionEvent actionEvent) {
        if (lagrangeStage == null) {
            lagrangeStage = initializeStage("", 300, 200, false, parentLagrange, Modality.WINDOW_MODAL, labelTitle.getScene().getWindow());
        }
        lagrangeStage.showAndWait();

        if (controller.getRunnableDoubleFunction() != null) {
            runnableDoubleFunction = controller.getRunnableDoubleFunction();
            labelStatus.setText("function was set in polynomial form ");
        }
    }

    public void action_buttonOk(ActionEvent actionEvent) {
        labelStatus.getScene().getWindow().hide();
    }

    public void action_buttonCancel(ActionEvent actionEvent) {
        runnableDoubleFunction = null;
        labelStatus.getScene().getWindow().hide();
    }

    public RunnableDoubleFunction getRunnableDoubleFunction() {
        return runnableDoubleFunction;
    }
}
