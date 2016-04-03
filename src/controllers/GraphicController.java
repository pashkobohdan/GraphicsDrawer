package controllers;

import controllers.settings.GraphicSettingsController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import library.function.RunnableDoubleFunction;
import library.function.settings.FunctionSettings;
import library.graphic.CanvasGraphic;
import library.graphic.settings.GraphicSettings;
import library.util.FXHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GraphicController implements Initializable {
    @FXML
    private MenuBar menuBar;
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

    private CanvasGraphic canvasGraphic;
    private RunnableDoubleFunction functionFX, functionGX;

    private FXMLLoader fxmlLoaderSetFX;
    private Parent parentSetFX;
    private ChoiceFunctionSetController choiceFunctionSetFXController;
    Stage choiceFunctionFXStage;

    private FXMLLoader fxmlLoaderSetGX;
    private Parent parentSetGX;
    private ChoiceFunctionSetController choiceFunctionSetGXController;
    Stage choiceFunctionGXStage;

    private FXMLLoader fxmlLoaderSettings;
    private Parent parentSettings;
    private GraphicSettingsController graphicSettingsController;
    Stage settingsStage;

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

        canvasGraphic = new CanvasGraphic(canvas, anchorPaneWithCanvas, new GraphicSettings());
        canvasGraphic.initialize();

        initializeWindows();

        initializeMenu();
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

    private void initializeSettingsWindow(){

    }

    private void initializeMenu() {
        Menu file = new Menu(resourceBundle.getString("menu.file.title"));
        Menu graphic = new Menu(resourceBundle.getString("menu.graphic.title"));
        Menu equation = new Menu(resourceBundle.getString("menu.equation.title"));
        Menu help = new Menu(resourceBundle.getString("menu.help.title"));

        // file
        MenuItem fileOpen = new MenuItem(resourceBundle.getString("menu.file.open"));
        MenuItem fileSave = new MenuItem(resourceBundle.getString("menu.file.save"));
        MenuItem fileSaveAs = new MenuItem(resourceBundle.getString("menu.file.saveAs"));
        MenuItem fileNew = new MenuItem(resourceBundle.getString("menu.file.new"));
        MenuItem fileExit = new MenuItem(resourceBundle.getString("menu.file.Exit"));
        fileOpen.setOnAction((event)->{

        });
        fileSave.setOnAction((event)->{

        });
        fileSaveAs.setOnAction((event)->{

        });
        fileNew.setOnAction((event)->{

        });
        fileExit.setOnAction((event)->{
            //save
            Platform.exit();
        });
        file.getItems().addAll(fileOpen, fileSave, fileSaveAs, fileNew, fileExit);

        // graphic
        MenuItem graphicRedraw = new MenuItem(resourceBundle.getString("menu.graphic.redraw"));
        MenuItem graphicSettings = new MenuItem(resourceBundle.getString("menu.graphic.settings"));
        MenuItem graphicSetEdges = new MenuItem(resourceBundle.getString("menu.graphic.setEdges"));
        graphicRedraw.setOnAction((event)->{
            canvasGraphic.refreshGraphic();
        });
        graphicSettings.setOnAction((event)->{
            if (settingsStage == null) {
                settingsStage = FXHelper.initializeStage("", 400, 550, false, parentSettings, Modality.WINDOW_MODAL, labelFunction1.getScene().getWindow());
            }
            settingsStage.showAndWait();
        });
        graphicSetEdges.setOnAction((event)->{

        });
        graphic.getItems().addAll(graphicRedraw, graphicSettings, graphicSetEdges);

        // equation
        MenuItem equationSearchRoots = new MenuItem(resourceBundle.getString("menu.equation.searchRoots"));
        equationSearchRoots.setOnAction((event)->{

        });
        equation.getItems().addAll(equationSearchRoots);

        // help
        MenuItem helpAbout = new MenuItem(resourceBundle.getString("menu.help.about"));
        MenuItem helpHelp = new MenuItem(resourceBundle.getString("menu.help.help"));
        helpAbout.setOnAction((event)->{

        });
        helpHelp.setOnAction((event)->{

        });
        help.getItems().addAll(helpAbout, helpHelp);


        menuBar.getMenus().addAll(file, graphic, equation, help);
    }

    private void initializeWindows() {
        try {
            // setters functions (different methods)
            fxmlLoaderSetFX = new FXMLLoader();
            fxmlLoaderSetFX.setLocation(getClass().getResource("../fxml/choiceMethods/choiceFunctionSet.fxml"));
            fxmlLoaderSetFX.setResources(resourceBundle);
            parentSetFX = fxmlLoaderSetFX.load();
            choiceFunctionSetFXController = fxmlLoaderSetFX.getController();

            fxmlLoaderSetGX = new FXMLLoader();
            fxmlLoaderSetGX.setLocation(getClass().getResource("../fxml/choiceMethods/choiceFunctionSet.fxml"));
            fxmlLoaderSetGX.setResources(resourceBundle);
            parentSetGX = fxmlLoaderSetGX.load();
            choiceFunctionSetGXController = fxmlLoaderSetGX.getController();

            //settings
            fxmlLoaderSettings = new FXMLLoader();
            fxmlLoaderSettings.setLocation(getClass().getResource("../fxml/settings/settings.fxml"));
            fxmlLoaderSettings.setResources(resourceBundle);
            parentSettings = fxmlLoaderSettings.load();
            graphicSettingsController = fxmlLoaderSettings.getController();
            graphicSettingsController.setGraphicSettings(canvasGraphic.getGraphicSettings());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void button_setFirstFunction(ActionEvent actionEvent) {
        if (choiceFunctionFXStage == null) {
            choiceFunctionFXStage = FXHelper.initializeStage("", 250, 300, false, parentSetFX, Modality.WINDOW_MODAL, labelFunction1.getScene().getWindow());
        }
        choiceFunctionFXStage.showAndWait();

        if (choiceFunctionSetFXController.getRunnableDoubleFunction() != null) {

            if (canvasGraphic.getFunctions() != null && canvasGraphic.getFunctions().get(0) != null) {
                canvasGraphic.getFunctions().remove(0);
            }
            functionFX = choiceFunctionSetFXController.getRunnableDoubleFunction();
            canvasGraphic.addFunction(0, functionFX);

            if (!canvasGraphic.isInitialised()) {
                canvasGraphic.initialize();
            }
            canvasGraphic.refreshGraphic();
        }
    }

    public void button_setSecondFunction(ActionEvent actionEvent) {
        if (choiceFunctionGXStage == null) {
            choiceFunctionGXStage = FXHelper.initializeStage("", 250, 300, false, parentSetGX, Modality.WINDOW_MODAL, labelFunction1.getScene().getWindow());
        }
        choiceFunctionGXStage.showAndWait();

        if (choiceFunctionSetGXController.getRunnableDoubleFunction() != null) {
            if (canvasGraphic.getFunctions() != null && canvasGraphic.getFunctions().size() > 1 && canvasGraphic.getFunctions().get(1) != null) {
                canvasGraphic.getFunctions().remove(1);
            }
            else{
                canvasGraphic.addFunction(null);
            }
            functionGX = choiceFunctionSetGXController.getRunnableDoubleFunction();
            canvasGraphic.addFunction(1, functionGX);

            if (!canvasGraphic.isInitialised()) {
                canvasGraphic.initialize();
            }
            canvasGraphic.refreshGraphic();
        }
    }
}
