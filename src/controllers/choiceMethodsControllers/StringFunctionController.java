package controllers.choiceMethodsControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import library.function.RunnableDoubleFunction;
import library.function.StringFunction;

/**
 * Created by pashk on 26.03.2016.
 */
public class StringFunctionController {
    @FXML
    private Label labelHeader, labelStatus;

    @FXML
    private TextField textFieldFunction;

    @FXML
    private Button buttonTest, buttonOk, buttonCancel;

    private RunnableDoubleFunction runnableDoubleFunction;


    public void action_buttonTest(ActionEvent actionEvent) {
        runnableDoubleFunction = new StringFunction(textFieldFunction.getText());

        labelStatus.setText("Please wait ...");

        if (runnableDoubleFunction.initialize() && runnableDoubleFunction.functionRun(1.0) != null) {
            labelStatus.setText("Status Ok !");
        } else {
            labelStatus.setText("Status Cancel !");
        }
    }

    public void action_buttonOk(ActionEvent actionEvent) {
        runnableDoubleFunction = new StringFunction(textFieldFunction.getText());

        if (!runnableDoubleFunction.initialize() || runnableDoubleFunction.functionRun(1.0) == null) {
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
        } else {

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
