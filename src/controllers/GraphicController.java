package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import library.function.RunnableDoubleFunction;
import library.graphic.CanvasGraphic;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GraphicController implements Initializable {
    @FXML
    private Label labelFunction1, labelFunction2;
    @FXML
    private Button buttonSetFunction1, buttonSetFunction2;
    @FXML
    private VBox rootVBox, vBoxWithControls;
    @FXML
    private AnchorPane anchorPaneWithCanvas;
    @FXML
    private Canvas canvas;

    private ResourceBundle resourceBundle;

    CanvasGraphic canvasGraphic;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;

        labelFunction1.setText(resources.getString("main.label.function1.text"));
        labelFunction2.setText(resources.getString("main.label.function2.text"));

        buttonSetFunction1.setText(resourceBundle.getString("main.button.setFunction"));
        buttonSetFunction2.setText(resourceBundle.getString("main.button.setFunction"));

        Canvas canvas = this.canvas;

//        StringFunction stringFunction = new StringFunction("x*sin(x)/5");
//        stringFunction.initialize();
//
//        PolynomialFunction polynomialFunction = new PolynomialFunction(1, 0, 0);
//        polynomialFunction.initialize();
//
//        PolynomialFunction polynomialFunction1 = new PolynomialFunction(-1, -2, 3);
//        polynomialFunction1.initialize();
//
//        LagrangePolynomialFunction lagrangePolynomialFunction = new LagrangePolynomialFunction(
//                new Point2D(0, -2),
//                new Point2D(1, -5),
//                new Point2D(2, 0),
//                new Point2D(3, -4)
//        );
//        lagrangePolynomialFunction.initialize();

        canvasGraphic = new CanvasGraphic(canvas, anchorPaneWithCanvas); // stringFunction, polynomialFunction, polynomialFunction1,lagrangePolynomialFunction
        canvasGraphic.setClearBeforeDrawing(true);
        canvasGraphic.setDrawCoordinateGrid(true);
        canvasGraphic.setDrawCurrentFunctionCoordinates(true);
        //canvasGraphic.setDrawCurrentMouseCoordinate(true);
        //canvasGraphic.setEnterMouseDots(true);
        canvasGraphic.initialize();

//        List<RunnableDoubleFunction> list = new LinkedList<RunnableDoubleFunction>();
//        list.add(stringFunction);
//        list.add(polynomialFunction);
//
//        System.out.println("\t\tSolvers : ");
//        for(double arg : new EqualTwoFunction().solve(polynomialFunction,stringFunction,-1.0,1.0,0.001,0.00000001)){
//            System.out.print(arg+" | ");
//        }
//        System.out.println();
    }

    public void button_setFirstFunction(ActionEvent actionEvent) {
        try {
            RunnableDoubleFunction runnableDoubleFunction = null;

            FXMLLoader fxmlLoaderChoiceFunctionSetMethod = new FXMLLoader();
            fxmlLoaderChoiceFunctionSetMethod.setLocation(getClass().getResource("../fxml/choiceFunctionSet.fxml"));
            fxmlLoaderChoiceFunctionSetMethod.setResources(resourceBundle);
            Parent parentFunctionSetMethod = fxmlLoaderChoiceFunctionSetMethod.load();
            ChoiceFunctionSetController choiceFunctionSetController = fxmlLoaderChoiceFunctionSetMethod.getController();

            Stage choiceFunctionSetMethodStage = new Stage();
//            choiceFunctionSetMethodStage.setTitle(resourceBundle.getString("key.dialog.newPlaylist.Title"));
            choiceFunctionSetMethodStage.setMinWidth(250);
            choiceFunctionSetMethodStage.setMinHeight(300);
            choiceFunctionSetMethodStage.setResizable(false);
            choiceFunctionSetMethodStage.setScene(new Scene(parentFunctionSetMethod));
            choiceFunctionSetMethodStage.initModality(Modality.WINDOW_MODAL);
            choiceFunctionSetMethodStage.initOwner(anchorPaneWithCanvas.getScene().getWindow());

            choiceFunctionSetMethodStage.showAndWait();

            if(choiceFunctionSetController.getRunnableDoubleFunction() != null){
                // to do
                canvasGraphic.addFunction(choiceFunctionSetController.getRunnableDoubleFunction());

                if(!canvasGraphic.isInitialised()){
                    canvasGraphic.initialize();
                }
                canvasGraphic.refreshGraphic();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void button_setSecondFunction(ActionEvent actionEvent) {
    }
}
