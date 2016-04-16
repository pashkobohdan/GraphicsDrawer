package controllers;

import controllers.graphicMethods.SetEdgesController;
import controllers.graphicMethods.SolveEquationController;
import controllers.settings.GraphicSettingsController;
import controllers.settings.MainSettingsController;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import library.function.RunnableDoubleFunction;
import library.graphic.CanvasGraphic;
import library.graphic.settings.WriteFunctionsSettings;
import library.graphic.settings.WriteGraphicSettings;
import library.graphic.settings.GraphicSettings;
import library.solver.EqualTwoDoubleFunction;
import library.util.FXHelper;
import library.util.LocaleInfo;
import library.util.ReadWrite;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GraphicController implements Initializable {

    public static final String PROJECT_PATH = System.getProperty("user.dir");
    public static final String SETTINGS_PATH = PROJECT_PATH + "/src/settings";
    public static final String SETTING_FILE = SETTINGS_PATH + "/graphicSettings.xml";

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

    private Stage stage;

    private ResourceBundle resourceBundle;

    private CanvasGraphic canvasGraphic;
    private RunnableDoubleFunction functionFX, functionGX;

    private FXMLLoader fxmlLoaderSetFX;
    private Parent parentSetFX;
    private ChoiceFunctionSetController choiceFunctionSetFXController;
    private Stage choiceFunctionFXStage;

    private FXMLLoader fxmlLoaderSetGX;
    private Parent parentSetGX;
    private ChoiceFunctionSetController choiceFunctionSetGXController;
    private Stage choiceFunctionGXStage;

    private FXMLLoader fxmlLoaderSettings;
    private Parent parentSettings;
    private GraphicSettingsController graphicSettingsController;
    private Stage settingsStage;

    private FXMLLoader fxmlLoaderMainSettings;
    private Parent parentMainSettings;
    private MainSettingsController mainSettingsController;
    private Stage mainSettingsStage;

    private FXMLLoader fxmlLoaderSetEdges;
    private Parent parentSetEdges;
    private SetEdgesController setEdgesController;
    private Stage setEdgesStage;

    private FXMLLoader fxmlLoaderSolveEquation;
    private Parent parentSolveEquation;
    private SolveEquationController solveEquationController;
    private Stage solveEquationStage;


    private boolean isOpenedSave;
    private String openedSavePath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;

        setTexts();

        Canvas canvas = this.canvas;
        canvasGraphic = new CanvasGraphic(canvas, anchorPaneWithCanvas, new GraphicSettings(), null, null);
        canvasGraphic.refreshGraphic();
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
//        for(double arg : new EqualTwoDoubleFunction().solve(polynomialFunction,stringFunction,-1.0,1.0,0.001,0.00000001)){
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
        MenuItem fileSettings = new MenuItem(resourceBundle.getString("menu.graphic.settings"));
        MenuItem fileExit = new MenuItem(resourceBundle.getString("menu.file.Exit"));

        fileOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        fileSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        fileSaveAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        fileNew.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        fileExit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));

        fileOpen.setOnAction((event) -> {
            showSaveCurrentFileAlert();
            openNewFile();
        });
        fileSave.setOnAction((event) -> {
            if (isOpenedSave) {
                WriteFunctionsSettings.write(openedSavePath, WriteFunctionsSettings.getByFunctions(
                        canvasGraphic.getFunctions().get(0),
                        canvasGraphic.getFunctions().get(1)
                ));

//                Alert info = new Alert(Alert.AlertType.INFORMATION, "Current file was saved to " + openedSavePath + " !");
//                info.setTitle("Title");
//                info.setHeaderText("Saving");
//                info.showAndWait();
            } else {
                saveAsFunctions();
            }
        });
        fileSaveAs.setOnAction((event) -> {
            saveAsFunctions();
        });
        fileNew.setOnAction((event) -> {
            showSaveCurrentFileAlert();

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML file(*.xml)", "*.xml"));
            File createFile = fileChooser.showSaveDialog(labelFunction1.getScene().getWindow());

            if (createFile != null) {
                reSetFunctions(null, null);
                canvasGraphic.setInitialised(false);
                canvasGraphic.refreshGraphic();

                WriteFunctionsSettings.write(createFile.getAbsolutePath(), WriteFunctionsSettings.getByFunctions(
                        canvasGraphic.getFunctions().get(0),
                        canvasGraphic.getFunctions().get(1)
                ));

                isOpenedSave = true;
                openedSavePath = createFile.getAbsolutePath();
            }
        });
        fileSettings.setOnAction((event) -> {
            if (mainSettingsStage == null) {
                mainSettingsStage = FXHelper.initializeStage("", 400, 550, false, parentMainSettings, Modality.WINDOW_MODAL, labelFunction1.getScene().getWindow());
                mainSettingsStage.setOnCloseRequest((closeEvent) -> {
                    if (mainSettingsController.isChange()) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Restart application after saving new language ?", ButtonType.OK, ButtonType.NO);
                        alert.setTitle("Title");
                        alert.setHeaderText("Saving");

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            showSaveCurrentFileAlert();

                            stage.close();

                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader();
                                fxmlLoader.setLocation(getClass().getResource("../fxml/graphic.fxml"));

                                String mainLanguage = LocaleInfo.readMainLanguage();
                                if (mainLanguage != null) {
                                    fxmlLoader.setResources(ResourceBundle.getBundle("bundles.Locale", new Locale(mainLanguage)));
                                } else {
                                    fxmlLoader.setResources(ResourceBundle.getBundle("bundles.Locale", Locale.getDefault()));
                                }

                                Stage primaryStage = new Stage();

                                Parent parentRoot = fxmlLoader.load();
                                GraphicController graphicController = fxmlLoader.getController();
                                graphicController.setStage(primaryStage);
                                primaryStage.setScene(new Scene(parentRoot, 600, 400));
                                primaryStage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
            //mainSettingsController.setGraphicSettings(canvasGraphic.getGraphicSettings());
            mainSettingsStage.showAndWait();
        });
        fileExit.setOnAction((event) -> {
            showSaveCurrentFileAlert();
            Platform.exit();
        });
        file.getItems().addAll(fileOpen, fileSave, fileSaveAs, fileNew, fileSettings, fileExit);

        // graphic subMenu
        MenuItem graphicRedraw = new MenuItem(resourceBundle.getString("menu.graphic.redraw"));
        MenuItem graphicSetEdges = new MenuItem(resourceBundle.getString("menu.graphic.setEdges"));
        MenuItem graphicResetCoordinate = new MenuItem(resourceBundle.getString("menu.graphic.resetSettings"));
        MenuItem graphicSettings = new MenuItem(resourceBundle.getString("menu.graphic.settings"));
        graphicRedraw.setOnAction((event) -> {
            canvasGraphic.refreshGraphic();
        });
        graphicResetCoordinate.setOnAction((event) -> {
            canvasGraphic.reSetCoordinateSystem();
        });
        graphicSetEdges.setOnAction((event) -> {
            if (setEdgesStage == null) {
                setEdgesStage = FXHelper.initializeStage("", 320, 200, false, parentSetEdges, Modality.WINDOW_MODAL, labelFunction1.getScene().getWindow());
            }
            setEdgesStage.showAndWait();
            if (setEdgesController.isCoordinatePresent()) {
                canvasGraphic.setCoordinateParameters(setEdgesController.getXFrom(), setEdgesController.getXTo(), setEdgesController.getYFrom(), setEdgesController.getYTo());
            }
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
        graphic.getItems().addAll(graphicRedraw, graphicResetCoordinate, graphicSetEdges, graphicSettings);

        // equation subMenu
        MenuItem equationSearchRoots = new MenuItem(resourceBundle.getString("menu.equation.searchRoots"));
        MenuItem equationClearRoots = new MenuItem(resourceBundle.getString("menu.equation.clearRoots"));
        equationSearchRoots.setOnAction((event) -> {
            if (canvasGraphic.getFunctions().get(0) == null || canvasGraphic.getFunctions().get(1) == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Some function was not installed", ButtonType.OK);
                alert.setTitle("Title");
                alert.setHeaderText("Error");
                alert.showAndWait();

                return;
            }

            if (solveEquationStage == null) {
                solveEquationStage = FXHelper.initializeStage("", 400, 170, false, parentSolveEquation, Modality.WINDOW_MODAL, labelFunction1.getScene().getWindow());
            }
            solveEquationStage.showAndWait();


            if (solveEquationController.isValuesPresent()) {
                List<Point2D> roots = new EqualTwoDoubleFunction().solve(canvasGraphic.getFunctions().get(0), canvasGraphic.getFunctions().get(1), solveEquationController.getXFrom(), solveEquationController.getXTo(), solveEquationController.getStep(), solveEquationController.getAccuracy());

                if (roots.size() == 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Roots are not exist !", ButtonType.OK);
                    alert.setTitle("Title");
                    alert.setHeaderText("Error");
                    alert.showAndWait();
                } else {
                    canvasGraphic.setRootsOfEquation(roots);
                    canvasGraphic.refreshGraphic();

                    ButtonType saveReport = new ButtonType("Save report");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK, saveReport);
                    alert.setTitle("Title");
                    alert.setHeaderText("Error");

                    String rootsString = "";
                    for (Point2D arg : roots) {
                        rootsString += String.format("x : %1$.5f\t\ty : %2$.5f\n", arg.getX(), arg.getY());
                    }

                    alert.setContentText("Roots : \n\n" + rootsString);
                    Optional<ButtonType> alertResult = alert.showAndWait();
                    if (alertResult.isPresent() && alertResult.get() == saveReport) {
                        saveReport(roots);
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Dta are not correct", ButtonType.OK);
                alert.setTitle("Title");
                alert.setHeaderText("Error");
                alert.showAndWait();
            }

        });
        equationClearRoots.setOnAction((event) -> {
            canvasGraphic.setRootsOfEquation(null);
            canvasGraphic.refreshGraphic();
        });
        equation.getItems().addAll(equationSearchRoots, equationClearRoots);

        // help subMenu
        MenuItem helpAbout = new MenuItem(resourceBundle.getString("menu.help.about"));
        MenuItem helpHelp = new MenuItem(resourceBundle.getString("menu.help.help"));
        helpAbout.setOnAction((event) -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Program to drawing graphics and searching roots\n(C) Pashko Bohdan");
            alert.setTitle("Title");
            alert.setHeaderText("About");
            alert.showAndWait();
        });
        helpHelp.setOnAction((event) -> {

        });
        help.getItems().addAll(helpAbout, helpHelp);

        menuBar.getMenus().addAll(file, graphic, equation, help);
    }

    private void saveReport(List<Point2D> roots) {

        WritableImage wim = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());

        canvas.snapshot(null, wim);

        int countOfReports = new File(PROJECT_PATH + "/src/reports/countOfReports.txt").exists() ? Integer.parseInt(ReadWrite.readMainLanguage(PROJECT_PATH + "/src/reports/countOfReports.txt")) : 0;

        File file = new File(PROJECT_PATH + "/src/reports/screenshots/screenshot_" + (countOfReports + 1) + ".png");
        ReadWrite.writeMainLanguage((countOfReports + 1) + "", PROJECT_PATH + "/src/reports/countOfReports.txt");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
        } catch (Exception e) {
            e.printStackTrace();
        }


        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE HTML PUBLIC \" -//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"><html> <head><meta http-equiv=\"Content - Type\" content=\"text / html; charset = windows - 1251\" /><style> h1{text-align:center;}table {border: 2px solid #ccc;margin: 0 auto;}th, td {border: 1px solid black;text - align: center;padding: 1em;}# left, #right {position: fixed;top: 5em;}# left {left: 5em;   }# center { margin: 0 auto;}# right {right: 5em;}# green {background: #81FF7A;}# red {background: #FF5252;}</style></head><body>");

        sb.append("\n<h1>Roots of equation :</h1>");
        sb.append("\n<h1>Снимок экрана :</h1>");
        sb.append("<center><img src=\"screenshots/screenshot_" + (countOfReports + 1) + ".png\" alt=\"picture\"></center>");

        sb.append("<TABLE>\n");
        sb.append("<TR>\n");

        sb.append("<TD>");
        sb.append("X");
        sb.append("</TD>");

        sb.append("<TD>");
        sb.append("Y");
        sb.append("</TD>");

        sb.append("</TR>\n");


        for (Point2D arg : roots) {
            sb.append("<TR>\n");
            sb.append("<TD>");
            sb.append(arg.getX());
            sb.append("</TD>");

            sb.append("<TD>");
            sb.append(arg.getY());
            sb.append("</TD>");

            sb.append("</TR>");
        }

        sb.append("</TABLE>");
        sb.append("</body>/n</ html > ");


        ReadWrite.writeMainLanguage(new String(sb), PROJECT_PATH + "/src/reports/report_" + (countOfReports + 1) + ".html");
    }

    private void showSaveCurrentFileAlert() {
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

//                Alert info = new Alert(Alert.AlertType.INFORMATION, "Current file was saved to " + openedSavePath + " !");
//                info.setTitle("Title");
//                info.setHeaderText("Saving");
//                info.showAndWait();
            }
        }
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

    private void reSetFunctions(RunnableDoubleFunction functionF, RunnableDoubleFunction functionG) {
        if (functionF != null) {
            functionF.initialize();
            labelFunction1.setTextFill(functionF.getFunctionSettings().getGraphicColor());
        }
        if (functionG != null) {
            functionG.initialize();
            labelFunction2.setTextFill(functionG.getFunctionSettings().getGraphicColor());
        }

        canvasGraphic.setFunctions(new LinkedList<>());
        canvasGraphic.getFunctions().add(functionF);
        canvasGraphic.getFunctions().add(functionG);

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

            fxmlLoaderMainSettings = new FXMLLoader();
            fxmlLoaderMainSettings.setLocation(getClass().getResource("../fxml/settings/mainSettings.fxml"));
            fxmlLoaderMainSettings.setResources(resourceBundle);
            parentMainSettings = fxmlLoaderMainSettings.load();
            mainSettingsController = fxmlLoaderMainSettings.getController();
            mainSettingsController.setGraphicController(this);


            // graphic methods
            fxmlLoaderSetEdges = new FXMLLoader();
            fxmlLoaderSetEdges.setLocation(getClass().getResource("../fxml/graphicMethods/setEdges.fxml"));
            fxmlLoaderSetEdges.setResources(resourceBundle);
            parentSetEdges = fxmlLoaderSetEdges.load();
            setEdgesController = fxmlLoaderSetEdges.getController();


            // equation
            fxmlLoaderSolveEquation = new FXMLLoader();
            fxmlLoaderSolveEquation.setLocation(getClass().getResource("../fxml/graphicMethods/solveEquation.fxml"));
            fxmlLoaderSolveEquation.setResources(resourceBundle);
            parentSolveEquation = fxmlLoaderSolveEquation.load();
            solveEquationController = fxmlLoaderSolveEquation.getController();

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
            reSetFunctions(canvasGraphic.getFunctions().get(0), choiceFunctionSetGXController.getRunnableDoubleFunction());
        }
    }

    public Scene getCurrentScene() {
        return labelFunction1.getScene();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        this.stage.setOnCloseRequest((event) -> {
            showSaveCurrentFileAlert();
        });
    }
}
