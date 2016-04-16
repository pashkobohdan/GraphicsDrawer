package controllers.settings;

import controllers.GraphicController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import library.util.FXHelper;
import library.util.LocaleInfo;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Bohdan Pashko on 07.04.2016.
 */
public class MainSettingsController implements Initializable {

    @FXML
    private Label labelSubTitleLanguages;

    @FXML
    private Label labelLanguage;

    @FXML
    private ComboBox comboBoxLanguages;

    private GraphicController graphicController;

    private String currentLanguage;
    private String choseLanguage;
    private boolean isChange;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // set texts

        currentLanguage = LocaleInfo.readMainLanguage();
        choseLanguage = currentLanguage;
        FXHelper.initializeAndSetCurrentComboBoxValue(currentLanguage, comboBoxLanguages, LocaleInfo.languagesList());
        comboBoxLanguages.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            choseLanguage = (String) newValue;
            if (!choseLanguage.equals(currentLanguage)) {
                isChange = true;
            } else {
                isChange = false;
            }
            LocaleInfo.writeMainLanguage(choseLanguage);
        });

    }

    public void setGraphicController(GraphicController graphicController) {
        this.graphicController = graphicController;
    }

    public String getChoseLanguage() {
        return choseLanguage;
    }

    public boolean isChange() {
        return isChange;
    }
}
