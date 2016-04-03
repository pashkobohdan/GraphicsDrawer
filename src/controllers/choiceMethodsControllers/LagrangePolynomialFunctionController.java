package controllers.choiceMethodsControllers;

import com.sun.javafx.scene.control.skin.TableViewSkin;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import library.function.LagrangePolynomialFunction;
import library.function.RunnableDoubleFunction;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Bohdan Pashko on 01.04.2016.
 */


public class LagrangePolynomialFunctionController implements Initializable {
    @FXML
    public TableView tableDots;

    @FXML
    public TableColumn columnX, columnY;

    @FXML
    public Button buttonOk, buttonCancel, buttonTest, buttonAdd, buttonRemove, buttonClear;

    @FXML
    public Label labelStatus;


    private RunnableDoubleFunction runnableDoubleFunction;

    private ObservableList<StringPoint> points;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        points = FXCollections.observableArrayList();
        points.add(new StringPoint("", ""));

        columnX.setCellValueFactory(new PropertyValueFactory<StringPoint, String>("x"));
        columnX.setCellFactory((t) -> new EditingCell());
        columnY.setCellValueFactory(new PropertyValueFactory<StringPoint, String>("y"));
        columnY.setCellFactory((t) -> new EditingCell());

        tableDots.setItems(points);
        tableDots.setEditable(true);

        columnX.setOnEditCommit(new EventHandler<CellEditEvent<StringPoint, String>>() {
            @Override
            public void handle(CellEditEvent<StringPoint, String> t) {
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setX(t.getNewValue());
            }
        });
        columnY.setOnEditCommit(new EventHandler<CellEditEvent<StringPoint, String>>() {
            @Override
            public void handle(CellEditEvent<StringPoint, String> t) {
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setY(t.getNewValue());
            }
        });

        ContextMenu tableContextMenu = new ContextMenu();
        MenuItem addMenuItem = new MenuItem("Add");
        MenuItem deleteSelectedMenuItem = new MenuItem("Delete selected");
        MenuItem clearAllItems = new MenuItem("Clear all");
        addMenuItem.setOnAction((event)->{
            try {
                points.add(tableDots.getSelectionModel().getFocusedIndex(), new StringPoint("", ""));
            }
            catch (Exception e){
                points.add(new StringPoint("", ""));
            }
        });
        deleteSelectedMenuItem.setOnAction((event)->{
            try {
                points.remove(tableDots.getSelectionModel().getFocusedIndex());
            }
            catch (Exception e){

            }
        });
        clearAllItems.setOnAction((event)->{
            points.clear();
            tableDots.setItems(points);
        });

        tableContextMenu.getItems().addAll(addMenuItem, deleteSelectedMenuItem, clearAllItems);
        tableDots.setContextMenu(tableContextMenu);
    }

    public void action_buttonOk(ActionEvent actionEvent) {
        if (!createFunction()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Функция некорректная ! Продолжить ввод ?", ButtonType.OK, ButtonType.CANCEL);
            alert.setTitle("Error");
            alert.setHeaderText("Function not corrected");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                labelStatus.setText("");
            } else {
                runnableDoubleFunction = null;
                labelStatus.getScene().getWindow().hide();
            }
        } else {
            labelStatus.getScene().getWindow().hide();
        }
    }

    public void action_buttonCancel(ActionEvent actionEvent) {
        runnableDoubleFunction = null;
        labelStatus.getScene().getWindow().hide();
    }

    public void action_buttonTest(ActionEvent actionEvent) {
        labelStatus.setText("please, wait ...");
        if (createFunction()) {
            labelStatus.setText("Status Ok !");
        } else {
            labelStatus.setText("Status Cancel !");
        }
    }

    public RunnableDoubleFunction getRunnableDoubleFunction() {
        return runnableDoubleFunction;
    }

    public void setRunnableDoubleFunction(RunnableDoubleFunction runnableDoubleFunction) {
        this.runnableDoubleFunction = runnableDoubleFunction;
    }

    public void action_buttonAdd(ActionEvent actionEvent) {
        points.add(new StringPoint("", ""));
    }

    public void action_buttonRemove(ActionEvent actionEvent) {
        try {
            points.remove(points.get(tableDots.getSelectionModel().getSelectedIndex()));
        } catch (IndexOutOfBoundsException e) {
            //e.printStackTrace();
        }
    }

    public void action_buttonClear(ActionEvent actionEvent) {
        points.clear();
        tableDots.setItems(points);
    }

    private boolean createFunction() {
        if (points == null || points.size() == 0) {
            //error !
            runnableDoubleFunction = null;
            return false;
        }
        List<Point2D> doublePoints = new LinkedList<>();
        double x, y;
        for (StringPoint arg : points) {
            if (arg.getX() == null || arg.getY() == null || arg.getX().equals("") || arg.getY().equals("")) {
                continue;
            }
            try {
                x = Double.parseDouble(arg.getX());
                y = Double.parseDouble(arg.getY());

                doublePoints.add(new Point2D(x, y));
            } catch (NumberFormatException e) {

            }
        }
        runnableDoubleFunction = new LagrangePolynomialFunction(doublePoints);
        try {
            if (!runnableDoubleFunction.initialize() || runnableDoubleFunction.functionRun(3.7333) == null) {
                runnableDoubleFunction = null;
                return false;
                //error !
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }


    public class StringPoint {
        private final SimpleStringProperty x;
        private final SimpleStringProperty y;

        public StringPoint(String x, String y) {
            this.x = new SimpleStringProperty(x);
            this.y = new SimpleStringProperty(y);
        }

        public String getX() {
            return x.get();
        }

        public void setX(String x) {
            this.x.set(x);
        }

        public String getY() {
            return y.get();
        }

        public void setY(String y) {
            this.y.set(y);
        }
    }

    public class EditingCell extends TableCell<StringPoint, String> {
        private TextField textField;

        public EditingCell() {
        }

        @Override
        public void startEdit() {
            super.startEdit();
            if (textField == null) {
                createTextField();
            }
            setGraphic(textField);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            Platform.runLater(() -> {
                textField.requestFocus();
                textField.selectAll();
            });
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getItem());
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setGraphic(textField);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                } else {
                    setText(getString());
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.setOnKeyPressed((KeyEvent t) -> {
                if (t.getCode() == KeyCode.ENTER) {
                    commitEdit(textField.getText());
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                } else if (t.getCode() == KeyCode.TAB) {
                    commitEdit(textField.getText());
                    getNextColumn(t.isShiftDown());
                }
            });
            textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (!newValue && textField != null) {
                    commitEdit(textField.getText());
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }

        private void getNextColumn(boolean forward) {

            List<TableColumn<StringPoint, ?>> columns = new ArrayList<>();
            for (TableColumn<StringPoint, ?> column : getTableView().getColumns()) {
                columns.addAll(getLeaves(column));
            }

            int currentIndex = columns.indexOf(getTableColumn());
            int nextIndex = currentIndex;

            if (columns.size() < 2) {
                getTableView().edit(getTableRow().getIndex() + 1, columns.get(0));
                return;
            }

            if (!forward) {
                nextIndex++;
                if (nextIndex > columns.size() - 1) {
                    if (getTableRow().getIndex() + 1 <= getTableView().getItems().size() - 1) {
                        getTableView().edit(getTableRow().getIndex() + 1, columns.get(0));
                        scrollDown(getTableRow().getIndex() + 1);
                    } else {
                        getTableView().edit(getTableRow().getIndex(), columns.get(0));
                    }
                    return;
                }
            } else {
                nextIndex--;
                if (nextIndex < 0) {
                    if (getTableRow().getIndex() - 1 >= 0) {
                        getTableView().edit(getTableRow().getIndex() - 1, columns.get(columns.size() - 1));
                        scrollUp(getTableRow().getIndex() - 1);
                    } else {
                        getTableView().edit(getTableRow().getIndex(), columns.get(columns.size() - 1));
                    }
                    return;
                }
            }
            getTableView().edit(getTableRow().getIndex(), columns.get(nextIndex));
        }

        private List<TableColumn<StringPoint, ?>> getLeaves(TableColumn<StringPoint, ?> root) {
            List<TableColumn<StringPoint, ?>> columns = new ArrayList<>();
            if (root.getColumns().isEmpty()) {
                if (root.isEditable()) {
                    columns.add(root);
                }
                return columns;
            } else {
                for (TableColumn<StringPoint, ?> column : root.getColumns()) {
                    columns.addAll(getLeaves(column));
                }
                return columns;
            }
        }

        private void scrollUp(int index) {
            TableViewSkin<?> ts = (TableViewSkin<?>) tableDots.getSkin();
            VirtualFlow<?> vf = (VirtualFlow<?>) ts.getChildren().get(1);

            int first = vf.getFirstVisibleCellWithinViewPort().getIndex();
            int last = vf.getLastVisibleCellWithinViewPort().getIndex();

            if (index <= (last + first) / 2) {
                vf.scrollTo(index - (last - first) / 2);

                ts = (TableViewSkin<?>) tableDots.getSkin();
                vf = (VirtualFlow<?>) ts.getChildren().get(1);
                first = vf.getFirstVisibleCellWithinViewPort().getIndex();
                last = vf.getLastVisibleCellWithinViewPort().getIndex();
            }
        }

        private void scrollDown(int index) {
            TableViewSkin<?> ts = (TableViewSkin<?>) tableDots.getSkin();
            VirtualFlow<?> vf = (VirtualFlow<?>) ts.getChildren().get(1);

            int first = vf.getFirstVisibleCellWithinViewPort().getIndex();
            int last = vf.getLastVisibleCellWithinViewPort().getIndex();

            if (index >= (last + first) / 2) {
                vf.scrollTo(index - ((last - first) / 2));

                ts = (TableViewSkin<?>) tableDots.getSkin();
                vf = (VirtualFlow<?>) ts.getChildren().get(1);
                first = vf.getFirstVisibleCellWithinViewPort().getIndex();
                last = vf.getLastVisibleCellWithinViewPort().getIndex();
            }
        }
    }

}
