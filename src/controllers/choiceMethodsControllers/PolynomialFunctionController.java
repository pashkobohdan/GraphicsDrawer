package controllers.choiceMethodsControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import library.function.PolynomialFunction;
import library.function.RunnableDoubleFunction;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Bohdan Pashko on 30.03.2016.
 */
public class PolynomialFunctionController {

    @FXML
    public Label labelHeader;

    @FXML
    public TextField textFieldFunction;

    @FXML
    public Label labelStatus;

    @FXML
    public Button buttonTest, buttonOk, buttonCancel;


    private RunnableDoubleFunction runnableDoubleFunction;


    public void action_buttonTest(ActionEvent actionEvent) {
        try {
            runnableDoubleFunction = new PolynomialFunction(Arrays
                    .stream(textFieldFunction.getText().split(","))
                    .map((String a) -> Double.parseDouble(a))
                    .collect(Collectors.toList()));

            if (!runnableDoubleFunction.initialize() || runnableDoubleFunction.functionRun(1.0) == null) {
                labelStatus.setText("Status Cancel !");
            } else {
                labelStatus.setText("Status Ok !");
            }
        } catch (Exception e) {
            labelStatus.setText("Status Cancel !");
        }
    }

    public void action_buttonOk(ActionEvent actionEvent) {
        try {
            runnableDoubleFunction = new PolynomialFunction(Arrays
                    .stream(textFieldFunction.getText().split(","))
                    .map((String a) -> Double.parseDouble(a))
                    .collect(Collectors.toList()));

            if (!runnableDoubleFunction.initialize() || runnableDoubleFunction.functionRun(1.0) == null) {
                showExitAlert();
            } else {
                textFieldFunction.getScene().getWindow().hide();
            }
        } catch (Exception e) {
            showExitAlert();
        }
    }

    private void showExitAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Функция некорректная ! Продолжить ввод ?", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Error");
        alert.setHeaderText("Function not corrected");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            textFieldFunction.setText("");
            labelStatus.setText("");
        } else {
            runnableDoubleFunction = null;
            textFieldFunction.getScene().getWindow().hide();
        }
    }

    public void action_buttonCancel(ActionEvent actionEvent) {
        runnableDoubleFunction = null;
        textFieldFunction.getScene().getWindow().hide();
    }

    public RunnableDoubleFunction getRunnableDoubleFunction() {
        return runnableDoubleFunction;
    }

    public void setRunnableDoubleFunction(RunnableDoubleFunction runnableDoubleFunction) {
        this.runnableDoubleFunction = runnableDoubleFunction;
    }
}
