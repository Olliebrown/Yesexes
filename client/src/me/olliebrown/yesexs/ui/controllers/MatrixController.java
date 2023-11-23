package me.olliebrown.yesexs.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import me.olliebrown.yesexs.core.Debugger;
import them.mdbell.util.MemValueType;

import javax.vecmath.Matrix4f;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javax.vecmath.*;

public class MatrixController implements IController {
    private MainController mc;

    @FXML
    private List<Label> matrixLabels;

    private Matrix4f m;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        m = new Matrix4f();
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

    public float[] getAsFloatArray() {
        return new float[] {
            m.m00, m.m01, m.m02, m.m03,
            m.m10, m.m11, m.m12, m.m13,
            m.m20, m.m21, m.m22, m.m23,
            m.m30, m.m31, m.m32, m.m33
        };
    }

    public int[] getAsIntArray() {
        float[] v = getAsFloatArray();
        int[] vi = new int[16];
        for (int i=0; i<16; i++) {
            vi[i] = Float.floatToIntBits(v[i]);
        }
        return vi;
    }

    public void updateMatrix (Matrix3f rot, Vector3f position, float scale) {
        m.set(rot, position, scale);
        updateLabels();
    }

    public void updateLabels () {
        float[] v = getAsFloatArray();
        for (int i = 0; i < 16; i++) {
            matrixLabels.get(i).setText(String.format("%.2f", v[i]));
        }
    }

    public void poke(long address) {
        int[] vi = getAsIntArray();
        Debugger debugger = mc.getDebugger();
        debugger.pokeArray(MemValueType.FLOAT, address, vi);
    }

    public void peek(long address) {
        int[] vi = getAsIntArray();
        Debugger debugger = mc.getDebugger();
        int[] rawData = debugger.peekArray(MemValueType.FLOAT, address, 16);

        m.m00 = Float.intBitsToFloat(rawData[0]);
        m.m01 = Float.intBitsToFloat(rawData[1]);
        m.m02 = Float.intBitsToFloat(rawData[2]);
        m.m03 = Float.intBitsToFloat(rawData[3]);

        m.m10 = Float.intBitsToFloat(rawData[4]);
        m.m11 = Float.intBitsToFloat(rawData[5]);
        m.m12 = Float.intBitsToFloat(rawData[6]);
        m.m13 = Float.intBitsToFloat(rawData[7]);

        m.m20 = Float.intBitsToFloat(rawData[8]);
        m.m21 = Float.intBitsToFloat(rawData[9]);
        m.m22 = Float.intBitsToFloat(rawData[10]);
        m.m23 = Float.intBitsToFloat(rawData[11]);

        m.m30 = Float.intBitsToFloat(rawData[12]);
        m.m31 = Float.intBitsToFloat(rawData[13]);
        m.m32 = Float.intBitsToFloat(rawData[14]);
        m.m33 = Float.intBitsToFloat(rawData[15]);
        updateLabels();
    }
}
