package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.util.LocaleInfo;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../fxml/graphic.fxml"));

        String mainLanguage = LocaleInfo.readMainLanguage();
        if (mainLanguage != null) {
            fxmlLoader.setResources(ResourceBundle.getBundle("bundles.Locale", new Locale(mainLanguage)));
        } else {
            fxmlLoader.setResources(ResourceBundle.getBundle("bundles.Locale", Locale.getDefault()));
        }

        Parent parentRoot = fxmlLoader.load();

        primaryStage.setScene(new Scene(parentRoot, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}













