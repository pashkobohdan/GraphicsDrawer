package library.graphic;

import com.sun.istack.internal.NotNull;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import library.doubleFunctions.Dividers;
import library.function.RunnableDoubleFunction;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CanvasGraphic {
    public static final double DEFAULT_SCALE = 100;
    public static final double INIT_WIDTH = 0.5;
    public static final double INIT_HEIGHT = 0.5;
    public static final double RUN_AWAY_GRAPHIC_SENSITIVITY = 0.05; // min[0..1]max
    public static final double MIN_PIXELS_IN_ONE_COORDINATE = 60;
    public static final double ZOOM_SENSITIVITY = 0.02; // [0...1]

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

    private boolean isClearBeforeDrawing;
    private boolean isDrawCoordinateGrid;
    private boolean isDrawCurrentPositionDots;

    private List<Double> horizontalLines, verticalLines;

    private double deltaPixelCenterX = 0, deltaPixelCenterY = 0;

    public CanvasGraphic(@NotNull Canvas canvas, @NotNull Region regionWithCanvas, @NotNull List<RunnableDoubleFunction> functions) {
        this.canvas = canvas;
        this.regionWithCanvas = regionWithCanvas;
        this.functions = functions;

        graphicsContext = canvas.getGraphicsContext2D();

        setClearBeforeDrawing(false);
        setDrawCoordinateGrid(false);
    }

    public CanvasGraphic(@NotNull Canvas canvas, @NotNull Region regionWithCanvas, @NotNull RunnableDoubleFunction... functions) {
        this.canvas = canvas;
        this.regionWithCanvas = regionWithCanvas;
        this.functions = Arrays.asList(functions);

        graphicsContext = canvas.getGraphicsContext2D();

        setClearBeforeDrawing(false);
        setDrawCoordinateGrid(false);
    }

    public void initialize() {
        initValues();
        initCanvasResize();
        initMouse();
    }

    private void initValues() {
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        scale = DEFAULT_SCALE;

        absoluteCenterX = INIT_WIDTH;
        absoluteCenterY = INIT_HEIGHT;
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
            isMouseEnter = false;
        });

        canvas.setOnMouseDragged((event) -> {
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

            if (isDrawCurrentPositionDots) {
                refreshGraphic();
                drawCurrentPositionDots(pixelMouseX);
            }
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

            scale = event.getDeltaY() > 0 ? scale * (1 + ZOOM_SENSITIVITY) : scale * (1 - ZOOM_SENSITIVITY);

            if (event.getDeltaY() > 0) {
                absoluteCenterX = (pixelMouseX - absoluteMouseX * scale) / canvasWidth;
                absoluteCenterY = (pixelMouseY - absoluteMouseY * scale) / canvasHeight;

            } else {
                absoluteCenterX += (pixelMouseX / canvasWidth - absoluteCenterX) * RUN_AWAY_GRAPHIC_SENSITIVITY;
                absoluteCenterY += (pixelMouseY / canvasHeight - absoluteCenterY) * RUN_AWAY_GRAPHIC_SENSITIVITY;

                absoluteMouseX = (pixelMouseX - absoluteCenterX * canvasWidth) / scale;
                absoluteMouseY = (pixelMouseY - absoluteCenterY * canvasHeight) / scale;
            }

            refreshGraphic();
        });
    }

    private void drawCurrentPositionDots(double pixelX) {
        for (RunnableDoubleFunction function : functions) {
            fillDot(pixelX, calculatePixelFunctionY(pixelX, function), 5);
        }
    }

    private void resizeCanvas() {
        refreshGraphic();
    }

    private void drawEdges() {
        graphicsContext.strokeRect(0, 0, canvasWidth, canvasHeight);
    }

    private List<Double> listOfCoordinateLines(double minAbsolute, double maxAbsolute, double heightOrWidth) {
        List<Double> result = new LinkedList<>();

        double step = Dividers.get125PowerMinDivider((maxAbsolute - minAbsolute) / (int) (heightOrWidth / MIN_PIXELS_IN_ONE_COORDINATE));
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
            if (isDrawCoordinateGrid) {
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
            if (isDrawCoordinateGrid) {
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

    private void refreshGraphic() {
        if (isClearBeforeDrawing()) {
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
        }

        graphicsContext.setStroke(Color.RED);
        drawEdges();

        graphicsContext.setStroke(Color.BLACK);
        drawCoordinateLines();

        horizontalLines = listOfCoordinateLines(calculateAbsoluteX(0), calculateAbsoluteX(canvasWidth), canvasWidth);
        verticalLines = listOfCoordinateLines(-absoluteCenterY * canvasHeight / scale, (1 - absoluteCenterY) * canvasHeight / scale, canvasHeight);
        graphicsContext.setStroke(new Color(Color.AQUA.getRed(), Color.AQUA.getGreen(), Color.AQUA.getBlue(), 0.25));
        drawCoordinateGrid();

        graphicsContext.setStroke(Color.RED);
        graphicsContext.setFont(new Font("Monospaced", 10));
        drawCoordinateValues();

        graphicsContext.setStroke(Color.BLACK);
        for (RunnableDoubleFunction function : functions) {
            drawGraphic(function);
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
//        if ((x1 + "").equals("NaN") || (y1 + "").equals("NaN") || (x2 + "").equals("NaN") || (y2 + "").equals("NaN")) {
//            System.out.println("\t\tNaN !");
//            return;
//        }

        graphicsContext.strokeLine(x1, canvasHeight - y1, x2, canvasHeight - y2);
    }

    public void strokeText(String text, double x, double y) {
        graphicsContext.strokeText(text, x, canvasHeight - y);
    }

    public void fillDot(double x, double y, double radius) {
        graphicsContext.fillOval(x - radius / 2, canvasHeight - y - radius / 2, radius, radius);
    }

    public boolean isClearBeforeDrawing() {
        return isClearBeforeDrawing;
    }

    public void setClearBeforeDrawing(boolean clearBeforeDrawing) {
        isClearBeforeDrawing = clearBeforeDrawing;
    }

    public boolean isDrawCoordinateGrid() {
        return isDrawCoordinateGrid;
    }

    public void setDrawCoordinateGrid(boolean drawCoordinateGrid) {
        isDrawCoordinateGrid = drawCoordinateGrid;
    }

    public boolean isDrawCurrentPositionDots() {
        return isDrawCurrentPositionDots;
    }

    public void setDrawCurrentPositionDots(boolean drawCurrentPositionDots) {
        isDrawCurrentPositionDots = drawCurrentPositionDots;
    }
}
