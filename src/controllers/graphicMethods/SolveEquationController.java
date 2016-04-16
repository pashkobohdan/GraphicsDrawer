package controllers.graphicMethods;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Bohdan Pashko on 08.04.2016.
 */
public class SolveEquationController implements Initializable {

    @FXML
    public Label labelTitle, labelXFrom, labelXTo, labelStep, labelAccuracy;

    @FXML
    public TextField textFieldXFrom, textFieldXTo, textFieldStep, textFieldAccuracy;

    @FXML
    public Button buttonOk, buttonCancel;

    private Double xFrom;
    private Double xTo;
    private Double step;
    private Double accuracy;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // text


        textFieldStep.setText("0.1");
        textFieldAccuracy.setText("0.001");
    }

    public void action_buttonOk(ActionEvent event) {
        xFrom = Double.parseDouble(textFieldXFrom.getText());
        xTo = Double.parseDouble(textFieldXTo.getText());
        step = Double.parseDouble(textFieldStep.getText());
        accuracy = Double.parseDouble(textFieldAccuracy.getText());

        labelAccuracy.getScene().getWindow().hide();
    }

    public void action_buttonCancel(ActionEvent event) {
        xFrom = null;
        xTo = null;
        step = null;
        accuracy = null;

        labelAccuracy.getScene().getWindow().hide();
    }

    public boolean isValuesPresent() {
        return xFrom != null && xTo != null && step != null && accuracy != null;
    }

    public Double getXFrom() {
        return xFrom;
    }

    public void setXFrom(Double xFrom) {
        this.xFrom = xFrom;
    }

    public Double getXTo() {
        return xTo;
    }

    public void setXTo(Double xTo) {
        this.xTo = xTo;
    }

    public Double getStep() {
        return step;
    }

    public void setStep(Double step) {
        this.step = step;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }
}
