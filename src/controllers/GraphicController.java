package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import library.function.PolynomialFunction;
import library.function.RunnableDoubleFunction;
import library.function.StringFunction;
import library.graphic.CanvasGraphic;
import library.solver.EqualTwoFunction;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class GraphicController implements Initializable {
    @FXML
    private VBox rootVBox, vBoxWithControls;

    @FXML
    private AnchorPane anchorPaneWithCanvas;

    @FXML
    private Canvas canvasGraphic;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Canvas canvas = canvasGraphic;

        StringFunction stringFunction = new StringFunction("x*sin(x)/5");
        stringFunction.initialize();

        PolynomialFunction polynomialFunction = new PolynomialFunction(1,0,0);
        polynomialFunction.initialize();

        PolynomialFunction polynomialFunction1 = new PolynomialFunction(-1,-2,3);
        polynomialFunction.initialize();

//        List<RunnableDoubleFunction> list = new LinkedList<RunnableDoubleFunction>();
//        list.add(stringFunction);
//        list.add(polynomialFunction);
//
//        System.out.println("\t\tSolvers : ");
//        for(double arg : new EqualTwoFunction().solve(polynomialFunction,stringFunction,-1.0,1.0,0.001,0.00000001)){
//            System.out.print(arg+" | ");
//        }
//        System.out.println();


        CanvasGraphic canvasGraphic = new CanvasGraphic(canvas, anchorPaneWithCanvas,stringFunction,polynomialFunction,polynomialFunction1);
        canvasGraphic.setClearBeforeDrawing(true);
        canvasGraphic.setDrawCoordinateGrid(true);
        canvasGraphic.setDrawCurrentPositionDots(true);
        canvasGraphic.initialize();

    }
}
