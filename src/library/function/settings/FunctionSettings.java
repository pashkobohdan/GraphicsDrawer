package library.function.settings;


import javafx.scene.paint.Color;

/**
 * Class for setting of current function (color, etc)
 * <p>
 * Created by Bohdan Pashko on 02.04.2016.
 */
public class FunctionSettings {

    /**
     * Default color (of course black)
     */
    public static final Color DEFAULT_FUNCTION_COLOR = Color.BLACK;

    /**
     * Color of current function
     */
    private Color graphicColor;

    public FunctionSettings() {
        graphicColor = DEFAULT_FUNCTION_COLOR;
    }

    public FunctionSettings(Color graphicColor) {
        this.graphicColor = graphicColor;
    }

    public Color getGraphicColor() {
        return graphicColor == null ? DEFAULT_FUNCTION_COLOR : graphicColor;
    }

    public void setGraphicColor(Color graphicColor) {
        this.graphicColor = graphicColor;
    }
}
