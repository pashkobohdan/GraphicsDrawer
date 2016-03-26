package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import library.function.LagrangePolynomialFunction;
import library.function.LagrangePolynomialScriptFunction;
import library.function.PolynomialFunction;
import library.function.StringFunction;
import library.graphic.CanvasGraphic;

import java.net.URL;
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

        PolynomialFunction polynomialFunction = new PolynomialFunction(1, 0, 0);
        polynomialFunction.initialize();

        PolynomialFunction polynomialFunction1 = new PolynomialFunction(-1, -2, 3);
        polynomialFunction1.initialize();



        LagrangePolynomialFunction lagrangePolynomialFunction = new LagrangePolynomialFunction(
                new Point2D(0, -2),
                new Point2D(1, -5),
                new Point2D(2, 0),
                new Point2D(3, -4)
//                new Point2D(0,0),
//                new Point2D(1.57,1),
//                new Point2D(3.14,0),
//                new Point2D(6.28,0),
//                new Point2D(-3.14,0)

        );
        lagrangePolynomialFunction.initialize();


        CanvasGraphic canvasGraphic = new CanvasGraphic(canvas, anchorPaneWithCanvas, stringFunction); // , polynomialFunction, polynomialFunction1,lagrangePolynomialFunction
        canvasGraphic.setClearBeforeDrawing(true);
        canvasGraphic.setDrawCoordinateGrid(true);
        canvasGraphic.setDrawCurrentPositionDots(true);
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
}
