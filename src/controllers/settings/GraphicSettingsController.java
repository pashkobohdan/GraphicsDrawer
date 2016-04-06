package controllers.settings;

import controllers.GraphicController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import library.graphic.settings.GraphicSettings;
import library.graphic.settings.WriteGraphicSettings;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Bohdan Pashko on 03.04.2016.
 */
public class GraphicSettingsController implements Initializable {
    public static final ObservableList<String> colors = FXCollections.observableArrayList(
            "aliceblue", "antiquewhite", "aqua", "aquamarine", "beige", "bisque", "black", "blanchedalmond", "blue", "brown", "chocolate", "coral", "cyan", "darkslategrey", "darkturquoise",
            "deepskyblue", "dimgray", "dodgerblue", "firebrick", "gold", "goldenrod", "gray", "green", "greenyellow", "grey", "indigo", "khaki", "lavender", "lightsteelblue",
            "lightyellow", "lime", "limegreen", "linen", "magenta", "mediumturquoise", "mediumvioletred", "moccasin", "navajowhite", "orange", "pink", "purple", "red", "skyblue", "springgreen",
            "steelblue", "tan", "tomato", "wheat", "white", "yellow");

    @FXML
    private Label labelSubTitleCheckBox;
    @FXML
    private CheckBox checkBoxClearBedoreDraw, checkBoxDrawCoordinateGrid, checkBoxDrawMouseCoordinate, checkBoxDrawFunctionValue, checkBoxDrawMouseClickDots;

    @FXML
    private Label labelSubTitleComboBox;
    @FXML
    private Label labelCBColorOfBackground, labelCBColorOfEdges, labelCBColorOfCoordinateLines, labelCBColorOfCoordinateValues, labelCBColorOfCoordinateGrid,
            labelCBColorOfFunctionValuesDots, labelCBColorOfMouseDots, labelCBColorOfRootsDots;
    @FXML
    private ComboBox comboBoxColorOfBackground, comboBoxColorOfEdges, comboBoxColorOfCoordinateLines, comboBoxColorOfCoordinateValues, comboBoxColorOfCoordinateGrid,
            comboBoxColorOfFunctionValuesDots, comboBoxColorOfMouseDots, comboBoxColorOfRootsDots;

    @FXML
    private Label labelSubTitleSlider;
    @FXML
    private Label labelSlMinOneLineSegment, labelSlZoomSensivity, labelSlRunAwaySensivity;
    @FXML
    private Slider sliderMinOneLineSegment, sliderZoomSensivity, sliderRunAwaySensivity;

    private ResourceBundle resourceBundle;

    private GraphicSettings graphicSettings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
    }

    public GraphicSettings getGraphicSettings() {
        return graphicSettings;
    }

    public void setGraphicSettings(GraphicSettings graphicSettings) {
        this.graphicSettings = graphicSettings;
        initializeBooleans();

        initializeColors();

        initializeSliders();

        labelSubTitleCheckBox.setText(resourceBundle.getString("settings.label.titleCheckBoxes"));
        labelSubTitleComboBox.setText(resourceBundle.getString("settings.label.titleColors"));
        labelSubTitleSlider.setText(resourceBundle.getString("settings.label.Values"));
    }

    private void initializeSliders() {
        labelSlMinOneLineSegment.setText(resourceBundle.getString("settings.label.MinLineSegmentWeight"));
        labelSlZoomSensivity.setText(resourceBundle.getString("settings.label.ZoonSensivity"));
        labelSlRunAwaySensivity.setText(resourceBundle.getString("settings.label.RunAwayGraphicSensivity"));

        setSliderMajors(sliderMinOneLineSegment, 20, 300, 40, 8, graphicSettings.getMinOneLineSegment());
        setSliderMajors(sliderZoomSensivity, 0, 1, 0.25, 0.05, graphicSettings.getZoomSensivity());
        setSliderMajors(sliderRunAwaySensivity, 0, 1, 0.25, 0.05, graphicSettings.getRunAwaySensivity());

        sliderMinOneLineSegment.valueProperty().addListener((observable, oldValue, newValue) -> {
            graphicSettings.setMinOneLineSegment((double) newValue);
        });
        sliderZoomSensivity.valueProperty().addListener((observable, oldValue, newValue) -> {
            graphicSettings.setZoomSensivity((double) newValue);
        });
        sliderRunAwaySensivity.valueProperty().addListener((observable, oldValue, newValue) -> {
            graphicSettings.setRunAwaySensivity((double) newValue);
        });
    }

    private void setSliderMajors(Slider slider, double min, double max, double tickUnit, double increment, double startValue) {
        slider.setMin(min);
        slider.setMax(max);
        slider.setMajorTickUnit(tickUnit);
        slider.setBlockIncrement(increment);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setValue(startValue);
    }

    private void initializeBooleans() {
        checkBoxClearBedoreDraw.setSelected(graphicSettings.isClearBeforeDrawing());
        checkBoxDrawCoordinateGrid.setSelected(graphicSettings.isDrawCoordinateGrid());
        checkBoxDrawMouseCoordinate.setSelected(graphicSettings.isDrawCurrentMouseCoordinate());
        checkBoxDrawFunctionValue.setSelected(graphicSettings.isDrawCurrentFunctionCoordinates());
        checkBoxDrawMouseClickDots.setSelected(graphicSettings.isEnterMouseDots());

        checkBoxClearBedoreDraw.setText(resourceBundle.getString("settings.checkBox.clearBeforeDrawing"));
        checkBoxDrawCoordinateGrid.setText(resourceBundle.getString("settings.checkBox.DrawCoordinateGrid"));
        checkBoxDrawMouseCoordinate.setText(resourceBundle.getString("settings.checkBox.DrawMouseCoordinate"));
        checkBoxDrawFunctionValue.setText(resourceBundle.getString("settings.checkBox.DrawFunctionsValues"));
        checkBoxDrawMouseClickDots.setText(resourceBundle.getString("settings.checkBox.DrawClickMouseDots"));

        checkBoxClearBedoreDraw.selectedProperty().addListener((observable, oldValue, newValue) -> {
            graphicSettings.setClearBeforeDrawing(newValue);
        });
        checkBoxDrawCoordinateGrid.selectedProperty().addListener((observable, oldValue, newValue) -> {
            graphicSettings.setDrawCoordinateGrid(newValue);
        });
        checkBoxDrawMouseCoordinate.selectedProperty().addListener((observable, oldValue, newValue) -> {
            graphicSettings.setDrawCurrentMouseCoordinate(newValue);
        });
        checkBoxDrawFunctionValue.selectedProperty().addListener((observable, oldValue, newValue) -> {
            graphicSettings.setDrawCurrentFunctionCoordinates(newValue);
        });
        checkBoxDrawMouseClickDots.selectedProperty().addListener((observable, oldValue, newValue) -> {
            graphicSettings.setEnterMouseDots(newValue);
        });
    }

    private void initializeColors() {
        labelCBColorOfBackground.setText(resourceBundle.getString("settings.label.BackgroundColor"));
        labelCBColorOfEdges.setText(resourceBundle.getString("settings.label.EdgesColor"));
        labelCBColorOfCoordinateLines.setText(resourceBundle.getString("settings.label.CoordinateLinesColor"));
        labelCBColorOfCoordinateValues.setText(resourceBundle.getString("settings.label.CoordinateValuesColor"));
        labelCBColorOfCoordinateGrid.setText(resourceBundle.getString("settings.label.CoordinateGridColor"));
        labelCBColorOfFunctionValuesDots.setText(resourceBundle.getString("settings.label.FunctionDotsColor"));
        labelCBColorOfMouseDots.setText(resourceBundle.getString("settings.label.MouseDotsColor"));
        labelCBColorOfRootsDots.setText(resourceBundle.getString("settings.label.RootsDotsColor"));

        comboBoxColorOfBackground.setItems(colors);
        comboBoxColorOfEdges.setItems(colors);
        comboBoxColorOfCoordinateLines.setItems(colors);
        comboBoxColorOfCoordinateValues.setItems(colors);
        comboBoxColorOfCoordinateGrid.setItems(colors);
        comboBoxColorOfFunctionValuesDots.setItems(colors);
        comboBoxColorOfMouseDots.setItems(colors);
        comboBoxColorOfRootsDots.setItems(colors);

        setCurrentColor(graphicSettings.getClearColor(), comboBoxColorOfBackground, colors);
        setCurrentColor(graphicSettings.getEdgesColor(), comboBoxColorOfEdges, colors);
        setCurrentColor(graphicSettings.getCoordinateLinesColor(), comboBoxColorOfCoordinateLines, colors);
        setCurrentColor(graphicSettings.getCoordinateValuesColor(), comboBoxColorOfCoordinateValues, colors);
        setCurrentColor(graphicSettings.getCoordinateGridColor(), comboBoxColorOfCoordinateGrid, colors);
        setCurrentColor(graphicSettings.getDotsOnFunctionsColor(), comboBoxColorOfFunctionValuesDots, colors);
        setCurrentColor(graphicSettings.getMouseDotsColor(), comboBoxColorOfMouseDots, colors);
        setCurrentColor(graphicSettings.getSolverDotsColor(), comboBoxColorOfRootsDots, colors);

        comboBoxColorOfBackground.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            graphicSettings.setClearColor(Color.web((String) newValue));
        });
        comboBoxColorOfEdges.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            graphicSettings.setEdgesColor(Color.web((String) newValue));
        });
        comboBoxColorOfCoordinateLines.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            graphicSettings.setCoordinateLinesColor(Color.web((String) newValue));
        });
        comboBoxColorOfCoordinateValues.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            graphicSettings.setCoordinateValuesColor(Color.web((String) newValue));
        });
        comboBoxColorOfCoordinateGrid.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            graphicSettings.setCoordinateGridColor(Color.web((String) newValue));
        });
        comboBoxColorOfFunctionValuesDots.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            graphicSettings.setDotsOnFunctionsColor(Color.web((String) newValue));
        });
        comboBoxColorOfMouseDots.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            graphicSettings.setMouseDotsColor(Color.web((String) newValue));
        });
        comboBoxColorOfRootsDots.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            graphicSettings.setSolverDotsColor(Color.web((String) newValue));
        });
    }

    public static void setCurrentColor(Color color, ComboBox<String> comboBox, List<String> colors) {
        for (int i = 0; i < colors.size(); i++) {
            if (color.equals(Color.web(colors.get(i)))) {
                comboBox.getSelectionModel().select(i);
                break;
            }
        }
        comboBox.setCellFactory((ist) -> new ListCell<String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                Rectangle rect = new Rectangle(18, 18);
                if (item != null) {
                    rect.setFill(Color.web(item));
                    setText(item);
                    setGraphic(rect);
                }
            }
        });
    }

}
