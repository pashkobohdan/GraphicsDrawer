package main;

import controllers.GraphicController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.util.LocaleInfo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {





        //System.out.println(new BigDecimal("2").divide(new BigDecimal("3")));
//
//        try{
//
//            for(int i=10;i>=0;--i){
//                System.out.println(i);
//                Thread.sleep(1000);
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//
//        }


        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../fxml/graphic.fxml"));

        String mainLanguage = LocaleInfo.readMainLanguage();
        if (mainLanguage != null) {
            fxmlLoader.setResources(ResourceBundle.getBundle("bundles.Locale", new Locale(mainLanguage)));
        } else {
            fxmlLoader.setResources(ResourceBundle.getBundle("bundles.Locale", Locale.getDefault()));
        }

        Parent parentRoot = fxmlLoader.load();
        GraphicController graphicController = fxmlLoader.getController();
        graphicController.setStage(primaryStage);
        primaryStage.setScene(new Scene(parentRoot, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}













