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

    private List<Double> horizontalLines, verticalLines;

    private double deltaPixelCenterX = 0, deltaPixelCenterY = 0;

    private boolean isInitialised;

    public CanvasGraphic(@NotNull Canvas canvas, @NotNull Region regionWithCanvas, GraphicSettings graphicSettings, @NotNull List<RunnableDoubleFunction> functions) {
        this.canvas = canvas;
        this.regionWithCanvas = regionWithCanvas;
        this.functions = functions;
        this.setGraphicSettings(graphicSettings);
        graphicSettings.setCanvasGraphic(this);

        graphicsContext = canvas.getGraphicsContext2D();
    }

    public CanvasGraphic(@NotNull Canvas canvas, @NotNull Region regionWithCanvas, GraphicSettings graphicSettings, @NotNull RunnableDoubleFunction... functions) {
        this.canvas = canvas;
        this.regionWithCanvas = regionWithCanvas;
        this.functions = Arrays.asList(functions);
        this.setGraphicSettings(graphicSettings);
        graphicSettings.setCanvasGraphic(this);

        graphicsContext = canvas.getGraphicsContext2D();
    }

    public CanvasGraphic(Canvas canvas, Region regionWithCanvas, GraphicSettings graphicSettings) {
        this.canvas = canvas;
        this.regionWithCanvas = regionWithCanvas;
        this.setGraphicSettings(graphicSettings);
        graphicSettings.setCanvasGraphic(this);

        graphicsContext = canvas.getGraphicsContext2D();
    }

    public void addFunction(RunnableDoubleFunction function) {
        if (functions == null) {
            functions = new LinkedList<>();
        }
        functions.add(function);
    }

    public void addFunction(int index, RunnableDoubleFunction function) {
        if (getFunctions() == null) {
            functions = new LinkedList<>();
        }
        getFunctions().add(index, function);
    }

    public void removeFunction(RunnableDoubleFunction function) {
        if (getFunctions() != null) {
            try {
                functions.remove(function);
            } catch (Exception e) {

            }
        }
    }

    public void initialize() {
        if (getFunctions() != null && getFunctions().size() != 0) {
            isInitialised = true;
            initValues();
            initCanvasResize();
            initMouse();
        }
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

    private List<Double> listOfCoordinateLines(double minAbsolute, double maxAbsolute, double heightOrWidth) {
        List<Double> result = new LinkedList<>();

        double step = Dividers.get125PowerMinDivider((maxAbsolute - minAbsolute) / (int) (heightOrWidth / graphicSettings.getMinOneLineSegment()));
        for (double i = (int) (minAbsolute / step) * step; i <= maxAbsolute; i += step) {
            if (Math.abs(i) < step / 2) {
                result.add(0.0);
            } else {
                result.add(Dividers.doubleToNormalValue(i));
            }
        }

        return result;
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
        if(!isInitialised){
            return;
        }
        if (getGraphicSettings().isClearBeforeDrawing()) {
            graphicsContext.setFill(getGraphicSettings().getClearColor());
            graphicsContext.fillRect(0, 0, canvasWidth, canvasHeight);
            //graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
        }

        graphicsContext.setStroke(getGraphicSettings().getEdgesColor());
        drawEdges();

        graphicsContext.setStroke(getGraphicSettings().getCoordinateLinesColor());
        drawCoordinateLines();

        horizontalLines = listOfCoordinateLines(calculateAbsoluteX(0), calculateAbsoluteX(canvasWidth), canvasWidth);
        verticalLines = listOfCoordinateLines(-absoluteCenterY * canvasHeight / scale, (1 - absoluteCenterY) * canvasHeight / scale, canvasHeight);

        graphicsContext.setStroke(getGraphicSettings().getCoordinateGridColor());
        drawCoordinateGrid();

        graphicsContext.setStroke(getGraphicSettings().getCoordinateValuesColor());
        graphicsContext.setFont(getGraphicSettings().getCoordinateValuesFont());
        drawCoordinateValues();

        for (RunnableDoubleFunction function : functions) {
            if (function != null) {
                graphicsContext.setStroke(function.getFunctionSettings().getGraphicColor());
                //graphicsContext.setLineWidth(3);
                drawGraphic(function);
            }
        }

        graphicsContext.setFill(getGraphicSettings().getMouseDotsColor());
        if (getGraphicSettings().isEnterMouseDots() && museClicksDots != null) {
            for (Point2D arg : museClicksDots) {
                fillDot(calculatePixelX(arg.getX()), calculatePixelY(arg.getY()), 7);
            }
        }

        if (canvas.isHover() && getGraphicSettings().isDrawCurrentFunctionCoordinates()) {
            graphicsContext.setFill(getGraphicSettings().getDotsOnFunctionsColor());
            drawCurrentPositionDots(pixelMouseX);
        }

        if (canvas.isHover() && getGraphicSettings().isDrawCurrentMouseCoordinate()) {
            graphicsContext.setStroke(getGraphicSettings().getMouseCoordinateColor());
            graphicsContext.setFont(getGraphicSettings().getMouseCoordinateFont());
            strokeText(Dividers.doubleToNormalValue(absoluteMouseX) + " : " + Dividers.doubleToNormalValue(absoluteMouseY), pixelMouseX, pixelMouseY);
        }
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
}
