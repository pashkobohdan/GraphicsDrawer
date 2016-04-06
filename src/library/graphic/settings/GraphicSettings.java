package library.graphic.settings;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import library.graphic.CanvasGraphic;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Settings of graphic with some functions
 *
 * Created by Bohdan Pashko on 02.04.2016.
 */
@XmlRootElement
public class GraphicSettings {

    private boolean clearBeforeDrawing;
    private boolean drawCoordinateGrid;
    private boolean drawCurrentFunctionCoordinates;
    private boolean drawCurrentMouseCoordinate;
    private boolean enterMouseDots;

    private Color coordinateLinesColor;
    private Color coordinateValuesColor;
    private Color coordinateGridColor;
    private Color dotsOnFunctionsColor;
    private Color mouseDotsColor;
    private Color solverDotsColor;
    private Color edgesColor;
    private Color mouseCoordinateColor;
    private Color clearColor;

    private Font coordinateValuesFont;
    private Font mouseCoordinateFont;

    private double minOneLineSegment;
    private double zoomSensivity;
    private double runAwaySensivity;

    /**
     * Default constructor (in my opinion - best of the best)
     */
    public GraphicSettings() {
        this.clearBeforeDrawing = true;
        this.drawCoordinateGrid = true;
        this.drawCurrentFunctionCoordinates = true;
        this.drawCurrentMouseCoordinate = false;
        this.enterMouseDots = false;
        this.coordinateLinesColor = Color.BLACK;
        this.coordinateValuesColor = Color.RED;
        this.coordinateGridColor = Color.BEIGE;
        this.dotsOnFunctionsColor = Color.BLACK;
        this.mouseDotsColor = Color.GREEN;
        this.solverDotsColor = Color.RED;
        this.edgesColor = Color.RED;
        this.setMouseCoordinateColor(Color.BLACK);
        this.clearColor = Color.WHITE;
        this.minOneLineSegment = 60;
        this.zoomSensivity = 0.2;
        this.runAwaySensivity = 0.5;

        setCoordinateValuesFont(new Font("Monospaced", 10));
        this.setMouseCoordinateFont(new Font("Monospaced",8));
    }

    /**
     * Custom constructor
     *
     * @param clearBeforeDrawing
     * @param drawCoordinateGrid
     * @param drawCurrentFunctionCoordinates
     * @param drawCurrentMouseCoordinate
     * @param enterMouseDots
     * @param coordinateLinesColor
     * @param coordinateValuesColor
     * @param coordinateGridColor
     * @param dotsOnFunctionsColor
     * @param mouseDotsColor
     * @param solverDotsColor
     * @param edgesColor
     * @param mouseCoordinateColor
     * @param clearColor
     * @param coordinateValuesFont
     * @param mouseCoordinateFont
     * @param minOneLineSegment
     * @param zoomSensivity
     * @param runAwaySensivity
     */
    public GraphicSettings(boolean clearBeforeDrawing, boolean drawCoordinateGrid, boolean drawCurrentFunctionCoordinates, boolean drawCurrentMouseCoordinate, boolean enterMouseDots, Color coordinateLinesColor, Color coordinateValuesColor, Color coordinateGridColor, Color dotsOnFunctionsColor, Color mouseDotsColor, Color solverDotsColor, Color edgesColor, Color mouseCoordinateColor, Color clearColor, Font coordinateValuesFont, Font mouseCoordinateFont, double minOneLineSegment, double zoomSensivity, double runAwaySensivity) {
        this.clearBeforeDrawing = clearBeforeDrawing;
        this.drawCoordinateGrid = drawCoordinateGrid;
        this.drawCurrentFunctionCoordinates = drawCurrentFunctionCoordinates;
        this.drawCurrentMouseCoordinate = drawCurrentMouseCoordinate;
        this.enterMouseDots = enterMouseDots;
        this.coordinateLinesColor = coordinateLinesColor;
        this.coordinateValuesColor = coordinateValuesColor;
        this.coordinateGridColor = coordinateGridColor;
        this.dotsOnFunctionsColor = dotsOnFunctionsColor;
        this.mouseDotsColor = mouseDotsColor;
        this.solverDotsColor = solverDotsColor;
        this.edgesColor = edgesColor;
        this.mouseCoordinateColor = mouseCoordinateColor;
        this.clearColor = clearColor;
        this.coordinateValuesFont = coordinateValuesFont;
        this.mouseCoordinateFont = mouseCoordinateFont;
        this.minOneLineSegment = minOneLineSegment;
        this.zoomSensivity = zoomSensivity;
        this.runAwaySensivity = runAwaySensivity;
    }

    public boolean isClearBeforeDrawing() {
        return clearBeforeDrawing;
    }

    public void setClearBeforeDrawing(boolean clearBeforeDrawing) {
        this.clearBeforeDrawing = clearBeforeDrawing;
    }

    public boolean isDrawCoordinateGrid() {
        return drawCoordinateGrid;
    }

    public void setDrawCoordinateGrid(boolean drawCoordinateGrid) {
        this.drawCoordinateGrid = drawCoordinateGrid;
    }

    public boolean isDrawCurrentFunctionCoordinates() {
        return drawCurrentFunctionCoordinates;
    }

    public void setDrawCurrentFunctionCoordinates(boolean drawCurrentFunctionCoordinates) {
        this.drawCurrentFunctionCoordinates = drawCurrentFunctionCoordinates;
    }

    public boolean isDrawCurrentMouseCoordinate() {
        return drawCurrentMouseCoordinate;
    }

    public void setDrawCurrentMouseCoordinate(boolean drawCurrentMouseCoordinate) {
        this.drawCurrentMouseCoordinate = drawCurrentMouseCoordinate;
    }

    public boolean isEnterMouseDots() {
        return enterMouseDots;
    }

    public void setEnterMouseDots(boolean enterMouseDots) {
        this.enterMouseDots = enterMouseDots;
    }

    public Color getCoordinateLinesColor() {
        return coordinateLinesColor;
    }

    public void setCoordinateLinesColor(Color coordinateLinesColor) {
        this.coordinateLinesColor = coordinateLinesColor;
    }

    public Color getCoordinateValuesColor() {
        return coordinateValuesColor;
    }

    public void setCoordinateValuesColor(Color coordinateValuesColor) {
        this.coordinateValuesColor = coordinateValuesColor;
    }

    public Color getCoordinateGridColor() {
        return coordinateGridColor;
    }

    public void setCoordinateGridColor(Color coordinateGridColor) {
        this.coordinateGridColor = coordinateGridColor;
    }

    public Color getDotsOnFunctionsColor() {
        return dotsOnFunctionsColor;
    }

    public void setDotsOnFunctionsColor(Color dotsOnFunctionsColor) {
        this.dotsOnFunctionsColor = dotsOnFunctionsColor;
    }

    public Color getMouseDotsColor() {
        return mouseDotsColor;
    }

    public void setMouseDotsColor(Color mouseDotsColor) {
        this.mouseDotsColor = mouseDotsColor;
    }

    public Color getSolverDotsColor() {
        return solverDotsColor;
    }

    public void setSolverDotsColor(Color solverDotsColor) {
        this.solverDotsColor = solverDotsColor;
    }

    public Color getEdgesColor() {
        return edgesColor;
    }

    public void setEdgesColor(Color edgesColor) {
        this.edgesColor = edgesColor;
    }

    public Font getCoordinateValuesFont() {
        return coordinateValuesFont;
    }

    public void setCoordinateValuesFont(Font coordinateValuesFont) {
        this.coordinateValuesFont = coordinateValuesFont;
    }

    public Font getMouseCoordinateFont() {
        return mouseCoordinateFont;
    }

    public void setMouseCoordinateFont(Font mouseCoordinateFont) {
        this.mouseCoordinateFont = mouseCoordinateFont;
    }

    public Color getMouseCoordinateColor() {
        return mouseCoordinateColor;
    }

    public void setMouseCoordinateColor(Color mouseCoordinateColor) {
        this.mouseCoordinateColor = mouseCoordinateColor;
    }

    public Color getClearColor() {
        return clearColor;
    }

    public void setClearColor(Color clearColor) {
        this.clearColor = clearColor;
    }

    public double getMinOneLineSegment() {
        return minOneLineSegment;
    }

    public void setMinOneLineSegment(double minOneLineSegment) {
        this.minOneLineSegment = minOneLineSegment;
    }

    public double getZoomSensivity() {
        return zoomSensivity;
    }

    public void setZoomSensivity(double zoomSensivity) {
        this.zoomSensivity = zoomSensivity;
    }

    public double getRunAwaySensivity() {
        return runAwaySensivity;
    }

    public void setRunAwaySensivity(double runAwaySensivity) {
        this.runAwaySensivity = runAwaySensivity;
    }
}
