package library.util;

import com.sun.istack.internal.NotNull;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Created by pashk on 02.04.2016.
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
}
