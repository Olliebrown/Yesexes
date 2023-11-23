package me.olliebrown.yesexs.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import them.mdbell.javafx.control.AddressSpinner;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.util.TimerTask;
import javax.vecmath.*;

public class PokerController implements IController {
    private MainController mc;

    @FXML
    private VBox pokerTabPage;

    @FXML
    private List<Spinner<Double>> translate;

    @FXML
    private List<Spinner<Double>> rotate;

    @FXML
    private Spinner<Double> scale;

    @FXML
    private GridPane newMatrix;

    @FXML
    private MatrixController newMatrixController;

    @FXML
    private GridPane readMatrix;

    @FXML
    private MatrixController readMatrixController;

    @FXML
    private CheckBox refreshCheckbox;

    @FXML
    private AddressSpinner matrixAddress;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        translate.get(0).valueProperty().addListener((obs, oldV, newV) -> updateMatrix());
        translate.get(1).valueProperty().addListener((obs, oldV, newV) -> updateMatrix());
        translate.get(2).valueProperty().addListener((obs, oldV, newV) -> updateMatrix());

        rotate.get(0).valueProperty().addListener((obs, oldV, newV) -> updateMatrix());
        rotate.get(1).valueProperty().addListener((obs, oldV, newV) -> updateMatrix());
        rotate.get(2).valueProperty().addListener((obs, oldV, newV) -> updateMatrix());

        scale.valueProperty().addListener((obs, oldV, newV) -> updateMatrix());
        updateMatrix();
    }

    @Override
    public void setMainController(MainController c) {
        mc = c;
        newMatrixController.setMainController(c);
        readMatrixController.setMainController(c);

        mc.timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (refreshCheckbox.isSelected()) {
                    mc.runAndWait(() -> peek());
                }
            }
        }, 1, 500);
    }

    @Override
    public void onConnect() {
        newMatrixController.onConnect();
        readMatrixController.onConnect();
    }

    @Override
    public void onDisconnect() {
        newMatrixController.onDisconnect();
        readMatrixController.onDisconnect();
    }

    private void updateMatrix () {
        Matrix3f rotX = new Matrix3f();
        Matrix3f rotY = new Matrix3f();
        Matrix3f rotZ = new Matrix3f();
        rotX.rotX(rotate.get(0).getValue().floatValue());
        rotY.rotY(rotate.get(1).getValue().floatValue());
        rotZ.rotZ(rotate.get(2).getValue().floatValue());

        rotY.mul(rotZ);
        rotX.mul(rotY);

        Vector3f position = new Vector3f(
            translate.get(0).getValue().floatValue(),
            translate.get(1).getValue().floatValue(),
            translate.get(2).getValue().floatValue()
        );

        newMatrixController.updateMatrix(rotX, position, scale.getValue().floatValue());
    }

    public void poke() {
        if (mc.getDebugger().connected()) {
            newMatrixController.poke(matrixAddress.getValue());
        }
    }

    public void peek() {
        if (mc.getDebugger().connected()) {
            readMatrixController.peek(matrixAddress.getValue());
        }
    }
}
