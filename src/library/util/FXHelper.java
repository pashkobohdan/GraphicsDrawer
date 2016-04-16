package library.util;

import com.sun.istack.internal.NotNull;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.List;

/**
 * Created by Bohdan Pashko on 02.04.2016.
 */
public class FXHelper {

    /**
     * @param title
     * @param minWidth
     * @param minHeight
     * @param resizable
     * @param parent
     * @param modality
     * @param owner
     * @return
     */
    public static Stage initializeStage(@NotNull String title, @NotNull int minWidth, @NotNull int minHeight, @NotNull boolean resizable, @NotNull Parent parent, @NotNull Modality modality, @NotNull Window owner) {
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

    public static void initializeAndSetCurrentComboBoxValue(String currentValue, ComboBox<String> comboBox, List<String> values) {
        comboBox.setItems(FXCollections.observableList(values));
        for (int i = 0; i < values.size(); i++) {
            if (currentValue.equals(values.get(i))) {
                comboBox.getSelectionModel().select(i);
                break;
            }
        }
    }
}
