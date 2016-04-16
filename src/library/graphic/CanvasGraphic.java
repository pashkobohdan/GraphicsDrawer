package library.graphic;

import com.sun.istack.internal.NotNull;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import library.doubleFunctions.Dividers;
import library.function.RunnableDoubleFunction;
import library.graphic.settings.GraphicSettings;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CanvasGraphic {
    public static final double DEFAULT_SCALE = 100;
    public static final double INIT_WIDTH = 0.5;
    public static final double INIT_HEIGHT = 0.5;

    private Canvas canvas;
    private Region regionWithCanvas;
    private GraphicsContext graphicsContext;
    private double canvasWidth, canvasHeight;

    private List<RunnableDoubleFunction> functions;

    private double scale;
    private double absoluteCenterX, absoluteCenterY;

    private double pixelMouseX, pixelMouseY;
    private double absoluteMouseX, absoluteMouseY;
    private boolean isMouseEnter = false;
    private boolean isMouseDragged;

    private GraphicSettings graphicSettings;
    private List<Point2D> museClicksDots;

    private List<Point2D> rootsOfEquation;

    private List<Double> horizontalLines, verticalLines;

    private double deltaPixelCenterX = 0, deltaPixelCenterY = 0;

    private boolean isInitialised;

    public CanvasGraphic(@NotNull Canvas canvas, @NotNull Region regionWithCanvas, @NotNull GraphicSettings graphicSettings, @NotNull List<RunnableDoubleFunction> functions) {
        this.canvas = canvas;
        this.regionWithCanvas = regionWithCanvas;
        this.setFunctions(functions);
        this.setGraphicSettings(graphicSettings);

        graphicsContext = canvas.getGraphicsContext2D();
    }

    public CanvasGraphic(@NotNull Canvas canvas, @NotNull Region regionWithCanvas, @NotNull GraphicSettings graphicSettings, @NotNull RunnableDoubleFunction... functions) {
        this.canvas = canvas;
        this.regionWithCanvas = regionWithCanvas;
        this.setFunctions(Arrays.asList(functions));
        this.setGraphicSettings(graphicSettings);

        graphicsContext = canvas.getGraphicsContext2D();
    }

    public CanvasGraphic(@NotNull Canvas canvas, @NotNull Region regionWithCanvas, @NotNull GraphicSettings graphicSettings) {
        this.canvas = canvas;
        this.regionWithCanvas = regionWithCanvas;
        this.setGraphicSettings(graphicSettings);

        graphicsContext = canvas.getGraphicsContext2D();
    }

    public void initialize() {
        if (isFunctionsCompetent()) {
            setInitialised(true);
            initValues();
            initCanvasResize();
            initMouse();
        }
    }

    private boolean isFunctionsCompetent() {
        if (functions == null || functions.size() == 0) {
            return false;
        }
        for (RunnableDoubleFunction f : functions) {
            if (f != null) {
                return true;
            }
        }
        return false;
    }

    private void initValues() {
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        scale = DEFAULT_SCALE;

        absoluteCenterX = INIT_WIDTH;
        absoluteCenterY = INIT_HEIGHT;

        canvas.setWidth(regionWithCanvas.getWidth());
        canvasWidth = regionWithCanvas.getWidth();
        canvas.setHeight(regionWithCanvas.getHeight());
        canvasHeight = regionWithCanvas.getHeight();
    }

    private void initCanvasResize() {
        regionWithCanvas.widthProperty().addListener((observable, oldValue, newValue) -> {
            canvas.setWidth(newValue.doubleValue());
            canvasWidth = newValue.doubleValue();

            resizeCanvas();
        });

        regionWithCanvas.heightProperty().addListener((observable, oldValue, newValue) -> {
            canvas.setHeight(newValue.doubleValue());
            canvasHeight = newValue.doubleValue();

            resizeCanvas();
        });
    }

    private void initMouse() {

        canvas.setOnMousePressed((event) -> {
            isMouseEnter = true;
            deltaPixelCenterX = event.getX();
            deltaPixelCenterY = canvasHeight - event.getY();
        });

        canvas.setOnMouseReleased((event) -> {
            if (!isMouseDragged && getGraphicSettings().isEnterMouseDots()) {
                if (museClicksDots == null) {
                    museClicksDots = new LinkedList<>();
                }
                museClicksDots.add(new Point2D((float) absoluteMouseX, (float) absoluteMouseY));
                refreshGraphic();
            }
            isMouseDragged = false;
            isMouseEnter = false;
        });

        canvas.setOnMouseDragged((event) -> {
            isMouseDragged = true;
            pixelMouseX = event.getX();
            pixelMouseY = canvasHeight - event.getY();

            absoluteMouseX = (pixelMouseX - absoluteCenterX * canvasWidth) / scale;
            absoluteMouseY = (pixelMouseY - absoluteCenterY * canvasHeight) / scale;

            absoluteCenterX += (event.getX() - deltaPixelCenterX) / canvasWidth;
            absoluteCenterY += ((canvasHeight - event.getY()) - deltaPixelCenterY) / canvasHeight;

            deltaPixelCenterX = event.getX();
            deltaPixelCenterY = canvasHeight - event.getY();

            refreshGraphic();
        });

        canvas.setOnMouseMoved((event) -> {
            pixelMouseX = event.getX();
            pixelMouseY = canvasHeight - event.getY();

            absoluteMouseX = (pixelMouseX - absoluteCenterX * canvasWidth) / scale;
            absoluteMouseY = (pixelMouseY - absoluteCenterY * canvasHeight) / scale;

            refreshGraphic();

            //System.out.println("mouseX:"+absoluteMouseX+"mouseY:"+absoluteMouseY);
        });

        canvas.setOnScroll((event) -> {
            if (isMouseEnter) {
                return;
            }

            if (scale < 4.0E-323 || scale > 1.0E307) {
                return;
            }

            if (scale < 0) {
                scale *= -1;
            }

            scale = event.getDeltaY() > 0 ? scale * (1 + graphicSettings.getZoomSensivity() / 10.0) : scale * (1 - graphicSettings.getZoomSensivity() / 10.0);

            if (event.getDeltaY() > 0) {
                absoluteCenterX = (pixelMouseX - absoluteMouseX * scale) / canvasWidth;
                absoluteCenterY = (pixelMouseY - absoluteMouseY * scale) / canvasHeight;

            } else {
                absoluteCenterX += (pixelMouseX / canvasWidth - absoluteCenterX) * graphicSettings.getRunAwaySensivity() / 10.0;
                absoluteCenterY += (pixelMouseY / canvasHeight - absoluteCenterY) * graphicSettings.getRunAwaySensivity() / 10.0;

                absoluteMouseX = (pixelMouseX - absoluteCenterX * canvasWidth) / scale;
                absoluteMouseY = (pixelMouseY - absoluteCenterY * canvasHeight) / scale;
            }

            refreshGraphic();
        });
    }

    private void drawCurrentPositionDots(double pixelX) {
        for (RunnableDoubleFunction function : functions) {
            if (function != null) {
                fillDot(pixelX, calculatePixelFunctionY(pixelX, function), 5);
            }
        }
    }

    private void drawCurrentMouseCoordinate(double pixelX, double pixelY, double absoluteX, double absoluteY) {
        strokeText(absoluteX + " : " + absoluteY, pixelX, pixelY);
    }

    private void resizeCanvas() {
        refreshGraphic();
    }

    private void drawEdges() {
        graphicsContext.strokeRect(0, 0, canvasWidth, canvasHeight);
    }

//    private List<Double> listOfCoordinateLines(double minAbsolute, double maxAbsolute, double heightOrWidth) {
//        List<Double> result = new LinkedList<>();
//
//        double step = Dividers.get125PowerMinDivider((maxAbsolute - minAbsolute) / (int) (heightOrWidth / graphicSettings.getMinOneLineSegment()));
//        for (double i = (int) (minAbsolute / step) * step; i <= maxAbsolute; i += step) {
//            if (Math.abs(i) < step / 2) {
//                result.add(0.0);
//            } else {
//                result.add(Dividers.doubleToNormalValue(i));
//            }
//        }
//
//        return result;
//    }

    private void calculateListsOfCoordinateLines(double minAbsoluteX, double maxAbsoluteX, double minAbsoluteY, double maxAbsoluteY, double heightOrWidth) {

//        horizontalLines = new LinkedList<>();
//        double step = Dividers.get125PowerMinDivider((maxAbsoluteX - minAbsoluteX) / (int) (heightOrWidth / graphicSettings.getMinOneLineSegment()));
//
//        for (double i = (int) (minAbsoluteX / step) * step; i <= maxAbsoluteX; i += step) {
//            if (Math.abs(i) < step / 2) {
//                horizontalLines.add(0.0);
//            } else {
//                horizontalLines.add(Dividers.doubleToNormalValue(i));
//            }
//        }
//
//
//        verticalLines = new LinkedList<>();
//        for (double i = (int) (minAbsoluteY / step) * step; i <= maxAbsoluteY; i += step) {
//            if (Math.abs(i) < step / 2) {
//                verticalLines.add(0.0);
//            } else {
//                verticalLines.add(Dividers.doubleToNormalValue(i));
//            }
//        }

        horizontalLines = new LinkedList<>();
        double step = Dividers.get125PowerMinDivider((maxAbsoluteX - minAbsoluteX) / (int) (heightOrWidth / graphicSettings.getMinOneLineSegment()));

        for (double i = (int) (minAbsoluteX / step) * step; i <= maxAbsoluteX; i += step) {
            if (Math.abs(i) < step / 2) {
                horizontalLines.add(0.0);
            } else {
                horizontalLines.add(Dividers.doubleToNormalValue(i));
            }
        }


        verticalLines = new LinkedList<>();
        for (double i = (int) (minAbsoluteY / step) * step; i <= maxAbsoluteY; i += step) {
            if (Math.abs(i) < step / 2) {
                verticalLines.add(0.0);
            } else {
                verticalLines.add(Dividers.doubleToNormalValue(i));
            }
        }
    }

    private void drawCoordinateGrid() {
        for (Double arg : horizontalLines) {
            if (getGraphicSettings().isDrawCoordinateGrid()) {
                if (arg != 0) {
                    strokeLine(calculatePixelX(arg), 0, calculatePixelX(arg), canvasHeight);
                }
            } else {
                strokeLine(calculatePixelX(arg), calculatePixelY(0) - 3, calculatePixelX(arg), calculatePixelY(0) + 3);
            }
        }

        for (Double arg : verticalLines) {
            if (arg == 0.0) {
                continue;
            }
            if (getGraphicSettings().isDrawCoordinateGrid()) {
                strokeLine(0, calculatePixelY(arg), canvasWidth, calculatePixelY(arg));
            } else {
                strokeLine(calculatePixelX(0) - 3, calculatePixelY(arg), calculatePixelX(0) + 3, calculatePixelY(arg));
            }
        }
    }

    private void drawCoordinateValues() {
        for (Double arg : horizontalLines) {
            if (absoluteCenterY >= 0 && absoluteCenterY <= 1) {
                if (absoluteCenterY > 0.5) {
                    strokeText(arg + "", calculatePixelX(arg), calculatePixelY(0) - 12);
                } else {
                    strokeText(arg + "", calculatePixelX(arg), calculatePixelY(0) + 5);
                }
            } else {
                if (absoluteCenterY < 0) {
                    strokeText(arg + "", calculatePixelX(arg), 5);
                } else {
                    strokeText(arg + "", calculatePixelX(arg), canvasHeight - graphicsContext.getFont().getSize() - 5);
                }
            }
        }

        for (Double arg : verticalLines) {
            if (arg == 0.0) {
                continue;
            }
            if (absoluteCenterX >= 0 && absoluteCenterX <= 1) {
                if (absoluteCenterX < 0.5) {
                    strokeText(arg + "", calculatePixelX(0) + 5, calculatePixelY(arg));
                } else {
                    strokeText(arg + "", calculatePixelX(0) - 20, calculatePixelY(arg));
                }
            } else {
                if (absoluteCenterX < 0) {
                    strokeText(arg + "", 5, calculatePixelY(arg));
                } else {
                    strokeText(arg + "", canvasWidth - 20, calculatePixelY(arg));
                }
            }
        }
    }

    private void drawCoordinateLines() {
        /*
        drawing x-line
         */
        if (absoluteCenterY > 0 && absoluteCenterY < 1) {
            strokeLine(0, calculatePixelY(0), canvasWidth, calculatePixelY(0));
        }

        /*
        drawing y-line
         */
        if (absoluteCenterX > 0 && absoluteCenterX < 1) {
            strokeLine(absoluteCenterX * canvasWidth, 0, absoluteCenterX * canvasWidth, canvasHeight);
        }
    }

    private void drawGraphic(RunnableDoubleFunction function) {
        double pixel_x = 0, pixel_y = calculatePixelFunctionY(pixel_x, function);
        for (double pixelX = 1; pixelX < canvasWidth; pixelX++) {
            double x = pixelX;
            double y = calculatePixelFunctionY(x, function);

            strokeLine(x, y, pixel_x, pixel_y);

            pixel_x = x;
            pixel_y = y;
        }
    }

    public void refreshGraphic() {
        if (!isInitialised()) {
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
            return;
        }

        // clearing screen if clear color was set and clearBeforeDrawing = true
        if (getGraphicSettings().isClearBeforeDrawing()) {
            graphicsContext.setFill(getGraphicSettings().getClearColor());
            graphicsContext.fillRect(0, 0, canvasWidth, canvasHeight);
        }

        // drawing canvas edges (default - red lines)
        graphicsContext.setStroke(getGraphicSettings().getEdgesColor());
        drawEdges();

        // drawing coordinate lines (OX and OY)
        graphicsContext.setStroke(getGraphicSettings().getCoordinateLinesColor());
        drawCoordinateLines();

        // drawing coordinate grid (if drawingCoordinateGrid = true)

        // deleted ...
        //horizontalLines = listOfCoordinateLines(calculateAbsoluteX(0), calculateAbsoluteX(canvasWidth), canvasWidth);
        //verticalLines = listOfCoordinateLines(-absoluteCenterY * canvasHeight / scale, (1 - absoluteCenterY) * canvasHeight / scale, canvasHeight);
        calculateListsOfCoordinateLines(calculateAbsoluteX(0), calculateAbsoluteX(canvasWidth), -absoluteCenterY * canvasHeight / scale, (1 - absoluteCenterY) * canvasHeight / scale,canvasWidth);
        graphicsContext.setStroke(getGraphicSettings().getCoordinateGridColor());
        drawCoordinateGrid();

        // drawing coordinate values
        graphicsContext.setStroke(getGraphicSettings().getCoordinateValuesColor());
        graphicsContext.setFont(getGraphicSettings().getCoordinateValuesFont());
        drawCoordinateValues();

        // drawing functions
        for (RunnableDoubleFunction function : functions) {
            if (function != null) {
                graphicsContext.setStroke(function.getFunctionSettings().getGraphicColor());
                drawGraphic(function);
            }
        }

        // drawing mouse dots (if museClicksDots = true)
        graphicsContext.setFill(getGraphicSettings().getMouseDotsColor());
        if (getGraphicSettings().isEnterMouseDots() && museClicksDots != null) {
            for (Point2D arg : museClicksDots) {
                fillDot(calculatePixelX(arg.getX()), calculatePixelY(arg.getY()), 7);
            }
        }

        // drawing roots dots of equation f(x) = g(x)
        if (rootsOfEquation != null && rootsOfEquation.size() != 0) {
            graphicsContext.setFill(getGraphicSettings().getSolverDotsColor());
            for (Point2D arg : rootsOfEquation) {
                fillDot(calculatePixelX(arg.getX()), calculatePixelY(arg.getY()), 9);
            }
        }

        // drawing dots on dunctions (if drawCurrentFunctionCoordinates = true)
        if (canvas.isHover() && getGraphicSettings().isDrawCurrentFunctionCoordinates()) {
            graphicsContext.setFill(getGraphicSettings().getDotsOnFunctionsColor());
            drawCurrentPositionDots(pixelMouseX);
        }

        // drawing mouse coordinates (if drawCurrentMouseCoordinate = true)
        if (canvas.isHover() && getGraphicSettings().isDrawCurrentMouseCoordinate()) {
            graphicsContext.setStroke(getGraphicSettings().getMouseCoordinateColor());
            graphicsContext.setFont(getGraphicSettings().getMouseCoordinateFont());
            strokeText(Dividers.doubleToNormalValue(absoluteMouseX) + " : " + Dividers.doubleToNormalValue(absoluteMouseY), pixelMouseX, pixelMouseY);
        }
    }

    public void reSetCoordinateSystem() {
        scale = DEFAULT_SCALE;

        absoluteCenterX = INIT_WIDTH;
        absoluteCenterY = INIT_HEIGHT;

        refreshGraphic();
    }

    public void setCoordinateParameters(double xFrom, double xTo, double yFrom, double yTo) {
        scale = Math.min(canvasWidth / Math.abs(xTo - xFrom), canvasHeight / Math.abs(yTo - yFrom));

        absoluteCenterX = 0.5 - (xFrom + xTo) / 2.0 * scale / canvasWidth;
        absoluteCenterY = 0.5 - (yFrom + yTo) / 2.0 * scale / canvasHeight;

        refreshGraphic();
    }

    private double calculatePixelX(double absoluteX) {
        return absoluteX * scale + absoluteCenterX * canvasWidth;
    }

    private double calculatePixelY(double absoluteY) {
        return absoluteY * scale + absoluteCenterY * canvasHeight;
    }

    private double calculateAbsoluteX(double pixelX) {
        return pixelX / scale - absoluteCenterX * canvasWidth / scale;
    }

    private double calculatePixelFunctionY(double x, RunnableDoubleFunction function) {
        return calculatePixelY(function.functionRun(calculateAbsoluteX(x)));
    }

    public void strokeLine(double x1, double y1, double x2, double y2) {
        graphicsContext.strokeLine(x1, canvasHeight - y1, x2, canvasHeight - y2);
    }

    public void strokeText(String text, double x, double y) {
        graphicsContext.strokeText(text, x, canvasHeight - y);
    }

    public void fillDot(double x, double y, double radius) {
        graphicsContext.fillOval(x - radius / 2.0, canvasHeight - y - radius / 2.0, radius, radius);
    }

    public boolean isInitialised() {
        return isInitialised;
    }

    public List<RunnableDoubleFunction> getFunctions() {
        return functions;
    }

    public GraphicSettings getGraphicSettings() {
        return graphicSettings;
    }

    public void setGraphicSettings(GraphicSettings graphicSettings) {
        this.graphicSettings = graphicSettings;
    }

    public void setFunctions(List<RunnableDoubleFunction> functions) {
        this.functions = functions;
    }

    public void setInitialised(boolean initialised) {
        isInitialised = initialised;
    }

    public List<Point2D> getRootsOfEquation() {
        return rootsOfEquation;
    }

    public void setRootsOfEquation(List<Point2D> rootsOfEquation) {
        this.rootsOfEquation = rootsOfEquation;

        if (rootsOfEquation == null) {
            return;
        }

        double yMin = rootsOfEquation.get(0).getY(), yMax = rootsOfEquation.get(0).getY();
        for (Point2D point : rootsOfEquation) {
            if (point.getY() > yMax) {
                yMax = point.getY();
            }
            if (point.getY() < yMin) {
                yMin = point.getY();
            }
        }

        setCoordinateParameters(rootsOfEquation.get(0).getX(), rootsOfEquation.get(rootsOfEquation.size() - 1).getX(), yMin, yMax);
    }
}
