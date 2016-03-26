package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.solver.EqualTwoFunction;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/graphic.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 600, 375));
        primaryStage.show();


        //Thread.sleep(10000);

//        for(double arg : Dividers.getListOfDividers(-10,60,0.003,0.06)){
//            System.out.println(arg);
//        }

//        System.out.println(Dividers.doubleToNormalValue(-9.92999999999999));
//        System.out.println(Dividers.doubleToNormalValue(55.29000000001));
//        System.out.println(Dividers.doubleToNormalValue(16.88500000000109));


//        System.out.println();
//        System.out.println();

//        for(double arg : Dividers.getListOfDividersFromCount(-17.5,60.6,10, 20)){
//            System.out.println(arg);
//        }


    }


    public static void main(String[] args) {
        launch(args);
    }
}













