package me.olliebrown.yesexs.ui.models;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ObservableNumberValue;

import java.nio.charset.StandardCharsets;

public class MemoryViewerTableModel {
    private final SimpleLongProperty addr;
    private final SimpleLongProperty[] longValues = new SimpleLongProperty[4];
    private final StringBinding[] asciiBindings = new StringBinding[16];

    public MemoryViewerTableModel() {
        this.addr = new SimpleLongProperty();
        this.longValues[0] = new SimpleLongProperty();
        this.longValues[1] = new SimpleLongProperty();
        this.longValues[2] = new SimpleLongProperty();
        this.longValues[3] = new SimpleLongProperty();
        for(int i = 0; i < asciiBindings.length; i++) {
            asciiBindings[i] = createBinding(i);
        }
        set(0,0,0,0,0);
    }

    private StringBinding createBinding(int i) {
        SimpleLongProperty prop = switch (i / 4) {
            case 0 -> longValues[0];
            case 1 -> longValues[1];
            case 2 -> longValues[2];
            case 3 -> longValues[3];
            default -> throw new IllegalArgumentException(String.valueOf(i));
        };
        int rem = i % 4;
        return Bindings.createStringBinding(() -> {
            int j = (prop.intValue() >> (24 - rem * 8)) & 0xFF;
            return new String(new byte[]{formatChar(j)}, StandardCharsets.UTF_8);
        }, prop);
    }

    private byte formatChar(int i) {
        if(i < 0x20 || i > 0x7E && i < 0xA1) {
            return '.';
        }
        return (byte)i;
    }

    public void set(long addr, int memVal1, int memVal2, int memVal3, int memVal4) {
        this.addr.set(addr);
        this.longValues[0].set(memVal1);
        this.longValues[1].set(memVal2);
        this.longValues[2].set(memVal3);
        this.longValues[3].set(memVal4);
    }

    public SimpleLongProperty addrProperty(){
        return addr;
    }

    public ObservableNumberValue firstValueProperty() {
        return longValues[0];
    }

    public ObservableNumberValue secondValueProperty() {
        return longValues[1];
    }

    public ObservableNumberValue thirdValueProperty() {
        return longValues[2];
    }

    public ObservableNumberValue fourthValueProperty() {
        return longValues[3];
    }

    public StringBinding asciiBinding(int i) {
        return asciiBindings[i];
    }
}