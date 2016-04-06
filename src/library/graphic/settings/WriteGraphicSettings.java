package library.graphic.settings;


import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;

/**
 * Created by Bohdan Pashko on 04.04.2016.
 */

@XmlRootElement
public class WriteGraphicSettings {
    private boolean clearBeforeDrawing;
    private boolean drawCoordinateGrid;
    private boolean drawCurrentFunctionCoordinates;
    private boolean drawCurrentMouseCoordinate;
    private boolean enterMouseDots;

    private String coordinateLinesColor;
    private String coordinateValuesColor;
    private String coordinateGridColor;
    private String dotsOnFunctionsColor;
    private String mouseDotsColor;
    private String solverDotsColor;
    private String edgesColor;
    private String mouseCoordinateColor;
    private String clearColor;

    private String coordinateValuesFont;
    private String mouseCoordinateFont;

    private double minOneLineSegment;
    private double zoomSensivity;
    private double runAwaySensivity;

    public WriteGraphicSettings(){

    }

    private WriteGraphicSettings(boolean clearBeforeDrawing, boolean drawCoordinateGrid, boolean drawCurrentFunctionCoordinates,
                                 boolean drawCurrentMouseCoordinate, boolean enterMouseDots, String coordinateLinesColor,
                                 String coordinateValuesColor, String coordinateGridColor, String dotsOnFunctionsColor,
                                 String mouseDotsColor, String solverDotsColor, String edgesColor, String mouseCoordinateColor,
                                 String clearColor, String coordinateValuesFont, String mouseCoordinateFont, double minOneLineSegment,
                                 double zoomSensivity, double runAwaySensivity) {
        this.setClearBeforeDrawing(clearBeforeDrawing);
        this.setDrawCoordinateGrid(drawCoordinateGrid);
        this.setDrawCurrentFunctionCoordinates(drawCurrentFunctionCoordinates);
        this.setDrawCurrentMouseCoordinate(drawCurrentMouseCoordinate);
        this.setEnterMouseDots(enterMouseDots);
        this.setCoordinateLinesColor(coordinateLinesColor);
        this.setCoordinateValuesColor(coordinateValuesColor);
        this.setCoordinateGridColor(coordinateGridColor);
        this.setDotsOnFunctionsColor(dotsOnFunctionsColor);
        this.setMouseDotsColor(mouseDotsColor);
        this.setSolverDotsColor(solverDotsColor);
        this.setEdgesColor(edgesColor);
        this.setMouseCoordinateColor(mouseCoordinateColor);
        this.setClearColor(clearColor);
        this.setCoordinateValuesFont(coordinateValuesFont);
        this.setMouseCoordinateFont(mouseCoordinateFont);
        this.setMinOneLineSegment(minOneLineSegment);
        this.setZoomSensivity(zoomSensivity);
        this.setRunAwaySensivity(runAwaySensivity);
    }

    public static WriteGraphicSettings getByGraphicSettings(GraphicSettings graphicSettings) {
        return new WriteGraphicSettings(
                graphicSettings.isClearBeforeDrawing(),
                graphicSettings.isDrawCoordinateGrid(),
                graphicSettings.isDrawCurrentFunctionCoordinates(),
                graphicSettings.isDrawCurrentMouseCoordinate(),
                graphicSettings.isEnterMouseDots(),
                graphicSettings.getCoordinateLinesColor().toString(),
                graphicSettings.getCoordinateValuesColor().toString(),
                graphicSettings.getCoordinateGridColor().toString(),
                graphicSettings.getDotsOnFunctionsColor().toString(),
                graphicSettings.getMouseDotsColor().toString(),
                graphicSettings.getSolverDotsColor().toString(),
                graphicSettings.getEdgesColor().toString(),
                graphicSettings.getMouseCoordinateColor().toString(),
                graphicSettings.getClearColor().toString(),
                graphicSettings.getCoordinateValuesFont().toString(),
                graphicSettings.getMouseCoordinateFont().toString(),
                graphicSettings.getMinOneLineSegment(),
                graphicSettings.getZoomSensivity(),
                graphicSettings.getRunAwaySensivity()
        );
    }

    public static GraphicSettings getGraphicSettings(WriteGraphicSettings writeGraphicSettings) {
        return new GraphicSettings(
                writeGraphicSettings.isClearBeforeDrawing(),
                writeGraphicSettings.isDrawCoordinateGrid(),
                writeGraphicSettings.isDrawCurrentFunctionCoordinates(),
                writeGraphicSettings.isDrawCurrentMouseCoordinate(),
                writeGraphicSettings.isEnterMouseDots(),
                Color.web(writeGraphicSettings.getCoordinateLinesColor()),
                Color.web(writeGraphicSettings.getCoordinateValuesColor().toString()),
                Color.web(writeGraphicSettings.getCoordinateGridColor().toString()),
                Color.web(writeGraphicSettings.getDotsOnFunctionsColor().toString()),
                Color.web(writeGraphicSettings.getMouseDotsColor().toString()),
                Color.web(writeGraphicSettings.getSolverDotsColor().toString()),
                Color.web(writeGraphicSettings.getEdgesColor().toString()),
                Color.web(writeGraphicSettings.getMouseCoordinateColor().toString()),
                Color.web(writeGraphicSettings.getClearColor().toString()),
                Font.font(writeGraphicSettings.getCoordinateValuesFont().toString()),
                Font.font(writeGraphicSettings.getMouseCoordinateFont().toString()),
                writeGraphicSettings.getMinOneLineSegment(),
                writeGraphicSettings.getZoomSensivity(),
                writeGraphicSettings.getRunAwaySensivity()
        );
    }

    public static boolean write(String fileName, GraphicSettings graphicSettings){
        try {
            File selectedFile = new File(fileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(WriteGraphicSettings.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(WriteGraphicSettings.getByGraphicSettings(graphicSettings), selectedFile);
            System.out.println("Write successful !");
        } catch (JAXBException e){
            e.printStackTrace();
        }
        
        return false;
    }
    
    public static GraphicSettings read(String fileName){
        if(!new File(fileName).exists()){
            return null;
        }
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(WriteGraphicSettings.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            WriteGraphicSettings customer = (WriteGraphicSettings) jaxbUnmarshaller.unmarshal(new File(fileName));
            return getGraphicSettings(customer);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    public String getCoordinateLinesColor() {
        return coordinateLinesColor;
    }

    public void setCoordinateLinesColor(String coordinateLinesColor) {
        this.coordinateLinesColor = coordinateLinesColor;
    }

    public String getCoordinateValuesColor() {
        return coordinateValuesColor;
    }

    public void setCoordinateValuesColor(String coordinateValuesColor) {
        this.coordinateValuesColor = coordinateValuesColor;
    }

    public String getCoordinateGridColor() {
        return coordinateGridColor;
    }

    public void setCoordinateGridColor(String coordinateGridColor) {
        this.coordinateGridColor = coordinateGridColor;
    }

    public String getDotsOnFunctionsColor() {
        return dotsOnFunctionsColor;
    }

    public void setDotsOnFunctionsColor(String dotsOnFunctionsColor) {
        this.dotsOnFunctionsColor = dotsOnFunctionsColor;
    }

    public String getMouseDotsColor() {
        return mouseDotsColor;
    }

    public void setMouseDotsColor(String mouseDotsColor) {
        this.mouseDotsColor = mouseDotsColor;
    }

    public String getSolverDotsColor() {
        return solverDotsColor;
    }

    public void setSolverDotsColor(String solverDotsColor) {
        this.solverDotsColor = solverDotsColor;
    }

    public String getEdgesColor() {
        return edgesColor;
    }

    public void setEdgesColor(String edgesColor) {
        this.edgesColor = edgesColor;
    }

    public String getMouseCoordinateColor() {
        return mouseCoordinateColor;
    }

    public void setMouseCoordinateColor(String mouseCoordinateColor) {
        this.mouseCoordinateColor = mouseCoordinateColor;
    }

    public String getClearColor() {
        return clearColor;
    }

    public void setClearColor(String clearColor) {
        this.clearColor = clearColor;
    }

    public String getCoordinateValuesFont() {
        return coordinateValuesFont;
    }

    public void setCoordinateValuesFont(String coordinateValuesFont) {
        this.coordinateValuesFont = coordinateValuesFont;
    }

    public String getMouseCoordinateFont() {
        return mouseCoordinateFont;
    }

    public void setMouseCoordinateFont(String mouseCoordinateFont) {
        this.mouseCoordinateFont = mouseCoordinateFont;
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
