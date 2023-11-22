package me.olliebrown.yesexs.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import me.olliebrown.yesexs.core.Debugger;
import me.olliebrown.yesexs.ui.models.DataType;
import them.mdbell.javafx.control.AddressSpinner;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
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
    private List<Label> matrixLabels ;

    @FXML
    private AddressSpinner pokeAddress;

    private Matrix4f m;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        m = new Matrix4f();

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
    }

    @Override
    public void onConnect() {

    }

    @Override
    public void onDisconnect() {

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

        m.set(rotX, position, scale.getValue().floatValue());
        updateLabels();
    }

    private void updateLabels () {
        float[] v = { m.m00, m.m01, m.m02, m.m03,
                      m.m10, m.m11, m.m12, m.m13,
                      m.m20, m.m21, m.m22, m.m23,
                      m.m30, m.m31, m.m32, m.m33 };

        for (int i = 0; i < 16; i++) {
            matrixLabels.get(i).setText(String.format("%.2f", v[i]));
        }
    }

    public void poke() {
        float[] v = { m.m00, m.m01, m.m02, m.m03,
                      m.m10, m.m11, m.m12, m.m13,
                      m.m20, m.m21, m.m22, m.m23,
                      m.m30, m.m31, m.m32, m.m33 };

        int[] vi = new int[16];
        for (int i=0; i<16; i++) {
            vi[i] = Float.floatToIntBits(v[i]);
        }

        Debugger debugger = mc.getDebugger();
        debugger.pokeArray(DataType.FLOAT, pokeAddress.getValue(), vi);
    }
}
