package me.olliebrown.yesexs.ui.models;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ObservableNumberValue;

public class SearchValueModel {

    private SimpleLongProperty addr;
    private SimpleLongProperty oldValue;
    private SimpleLongProperty newValue;
    private ObservableNumberValue diff;


    public SearchValueModel(long addr, long prev, long current) {
        init(addr, prev, current);
    }

    private void init(long addr, long prev, long current) {
        this.addr = new SimpleLongProperty(addr);
        this.oldValue = new SimpleLongProperty(prev);
        this.newValue = new SimpleLongProperty(current);
        this.diff = Bindings.subtract(newValue, oldValue);
    }

    public SimpleLongProperty addrProperty() {
        return addr;
    }

    public SimpleLongProperty oldValueProperty() {
        return oldValue;
    }

    public SimpleLongProperty newValueProperty() {
        return newValue;
    }

    public ObservableNumberValue diffProperty() {
        return diff;
    }

    public long getAddr() {
        return addr.get();
    }

    public long getOldValue() {
        return oldValue.get();
    }

    public long getNewValue() {
        return newValue.get();
    }
}
