package controllers;

import controllers.settings.GraphicSettingsController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import library.function.RunnableDoubleFunction;
import library.graphic.CanvasGraphic;
import library.graphic.settings.WriteFunctionsSettings;
import library.graphic.settings.WriteGraphicSettings;
import library.graphic.settings.GraphicSettings;
import library.util.FXHelper;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
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

    public static final String PROJECT_PATH = System.getProperty("user.dir");
    public static final String SETTINGS_PATH = PROJECT_PATH + "/src/settings";
    public static final String SETTING_FILE = SETTINGS_PATH + "/graphicSettings.xml";

    private boolean isOpenedSave;
    private String openedSavePath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;

        setTexts();

        Canvas canvas = this.canvas;
        canvasGraphic = new CanvasGraphic(canvas, anchorPaneWithCanvas, new GraphicSettings(), null, null);
        initializeWindows();
        initializeMenu();
        checkGraphicSettings();

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

    private void setTexts() {
        labelFunction1.setText(resourceBundle.getString("main.label.function1.text"));
        labelFunction2.setText(resourceBundle.getString("main.label.function2.text"));

        buttonSetFunction1.setText(resourceBundle.getString("main.button.setFunction"));
        buttonSetFunction2.setText(resourceBundle.getString("main.button.setFunction"));
    }

    private void checkGraphicSettings() {
        GraphicSettings graphicSettings = WriteGraphicSettings.read(SETTING_FILE);
        if (graphicSettings != null) {
            canvasGraphic.setGraphicSettings(graphicSettings);
        } else {
            WriteGraphicSettings.write(SETTING_FILE, canvasGraphic.getGraphicSettings());
        }
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

        fileOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        fileSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        fileSaveAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        fileNew.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        fileExit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));

        fileOpen.setOnAction((event) -> {
            if (isOpenedSave) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Title");
                alert.setHeaderText("Do you want to save current file before open new file ?");
                alert.setContentText("Save ?");

                if (alert.showAndWait().get() == ButtonType.OK) {
                    WriteFunctionsSettings.write(openedSavePath, WriteFunctionsSettings.getByFunctions(
                            canvasGraphic.getFunctions().get(0),
                            canvasGraphic.getFunctions().get(1)
                    ));

                    Alert info = new Alert(Alert.AlertType.INFORMATION,"Current file was saved to "+openedSavePath+" !");
                    info.setTitle("Title");
                    info.setHeaderText("Saving");
                    info.showAndWait();
                }
            }
            openNewFile();
        });
        fileSave.setOnAction((event) -> {
            if(isOpenedSave){
                WriteFunctionsSettings.write(openedSavePath, WriteFunctionsSettings.getByFunctions(
                        canvasGraphic.getFunctions().get(0),
                        canvasGraphic.getFunctions().get(1)
                ));

                Alert info = new Alert(Alert.AlertType.INFORMATION,"Current file was saved to "+openedSavePath+" !");
                info.setTitle("Title");
                info.setHeaderText("Saving");
                info.showAndWait();
            }
            else{
                saveAsFunctions();
            }
        });
        fileSaveAs.setOnAction((event) -> {
            saveAsFunctions();
        });
        fileNew.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML file(*.xml)", "*.xml"));
            File createFile = fileChooser.showSaveDialog(labelFunction1.getScene().getWindow());

            if (createFile != null) {
                WriteFunctionsSettings.write(createFile.getAbsolutePath(), WriteFunctionsSettings.getByFunctions(
                        canvasGraphic.getFunctions().get(0),
                        canvasGraphic.getFunctions().get(1)
                ));

                isOpenedSave = true;
                openedSavePath = createFile.getAbsolutePath();
            }
        });
        fileExit.setOnAction((event) -> {
            //save
            Platform.exit();
        });
        file.getItems().addAll(fileOpen, fileSave, fileSaveAs, fileNew, fileExit);

        // graphic
        MenuItem graphicRedraw = new MenuItem(resourceBundle.getString("menu.graphic.redraw"));
        MenuItem graphicSettings = new MenuItem(resourceBundle.getString("menu.graphic.settings"));
        MenuItem graphicSetEdges = new MenuItem(resourceBundle.getString("menu.graphic.setEdges"));
        graphicRedraw.setOnAction((event) -> {
            canvasGraphic.refreshGraphic();
        });
        graphicSettings.setOnAction((event) -> {
            if (settingsStage == null) {
                settingsStage = FXHelper.initializeStage("", 400, 550, false, parentSettings, Modality.WINDOW_MODAL, labelFunction1.getScene().getWindow());
            }
            graphicSettingsController.setGraphicSettings(canvasGraphic.getGraphicSettings());
            settingsStage.showAndWait();
            canvasGraphic.refreshGraphic();

            WriteGraphicSettings.write(GraphicController.SETTING_FILE, canvasGraphic.getGraphicSettings());
        });
        graphicSetEdges.setOnAction((event) -> {

        });
        graphic.getItems().addAll(graphicRedraw, graphicSettings, graphicSetEdges);

        // equation
        MenuItem equationSearchRoots = new MenuItem(resourceBundle.getString("menu.equation.searchRoots"));
        equationSearchRoots.setOnAction((event) -> {

        });
        equation.getItems().addAll(equationSearchRoots);

        // help
        MenuItem helpAbout = new MenuItem(resourceBundle.getString("menu.help.about"));
        MenuItem helpHelp = new MenuItem(resourceBundle.getString("menu.help.help"));
        helpAbout.setOnAction((event) -> {

        });
        helpHelp.setOnAction((event) -> {

        });
        help.getItems().addAll(helpAbout, helpHelp);


        menuBar.getMenus().addAll(file, graphic, equation, help);

    }

    private void saveAsFunctions() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML file(*.xml)", "*.xml"));
        File file1 = fileChooser.showSaveDialog(labelFunction1.getScene().getWindow());

        if (file1 != null) {
            WriteFunctionsSettings.write(file1.getAbsolutePath(), WriteFunctionsSettings.getByFunctions(
                    canvasGraphic.getFunctions().get(0),
                    canvasGraphic.getFunctions().get(1)
            ));
        }
    }

    private void openNewFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML file(*.xml)", "*.xml"));
        File readFile = fileChooser.showOpenDialog(labelFunction1.getScene().getWindow());

        if (readFile != null) {
            isOpenedSave = true;
            openedSavePath = readFile.getAbsolutePath();

            WriteFunctionsSettings fs = WriteFunctionsSettings.read(readFile.getAbsolutePath());

            reSetFunctions(fs.getRunnableFunctionF(), fs.getRunnableFunctionG());

            isOpenedSave = true;
            openedSavePath = readFile.getAbsolutePath();
        }
    }

    private void reSetFunctions(RunnableDoubleFunction functionF, RunnableDoubleFunction functionG){
        if(functionF!=null) {
            functionF.initialize();
            labelFunction1.setTextFill(functionF.getFunctionSettings().getGraphicColor());
        }
        if(functionG!=null) {
            functionG.initialize();
            labelFunction2.setTextFill(functionG.getFunctionSettings().getGraphicColor());
        }

        canvasGraphic.setFunctions(new LinkedList<>());
        canvasGraphic.addFunction(functionF);
        canvasGraphic.addFunction(functionG);

        if (!canvasGraphic.isInitialised()) {
            canvasGraphic.initialize();
        }
        canvasGraphic.refreshGraphic();
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
        choiceFunctionSetFXController.setRunnableDoubleFunction(canvasGraphic.getFunctions().get(0));
        choiceFunctionFXStage.showAndWait();

        if (choiceFunctionSetFXController.getRunnableDoubleFunction() != null) {
            reSetFunctions(choiceFunctionSetFXController.getRunnableDoubleFunction(), canvasGraphic.getFunctions().get(1));
        }
    }

    public void button_setSecondFunction(ActionEvent actionEvent) {
        if (choiceFunctionGXStage == null) {
            choiceFunctionGXStage = FXHelper.initializeStage("", 250, 300, false, parentSetGX, Modality.WINDOW_MODAL, labelFunction1.getScene().getWindow());
        }
        choiceFunctionSetGXController.setRunnableDoubleFunction(canvasGraphic.getFunctions().get(1));
        choiceFunctionGXStage.showAndWait();

        if (choiceFunctionSetGXController.getRunnableDoubleFunction() != null) {
            reSetFunctions( canvasGraphic.getFunctions().get(0), choiceFunctionSetGXController.getRunnableDoubleFunction());
        }
    }
}
