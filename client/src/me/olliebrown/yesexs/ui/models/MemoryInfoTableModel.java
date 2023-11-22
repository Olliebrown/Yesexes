package me.olliebrown.yesexs.ui.models;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import me.olliebrown.yesexs.core.MemoryInfo;
import me.olliebrown.yesexs.core.MemoryType;

public class MemoryInfoTableModel {

    private final SimpleStringProperty name;
    private final SimpleLongProperty addr;
    private final SimpleLongProperty size;
    private final NumberBinding end;
    private final SimpleObjectProperty<MemoryType> type;
    private final SimpleIntegerProperty access;

    public MemoryInfoTableModel(MemoryInfo info) {
        this("-", info);
    }

    public MemoryInfoTableModel(String name, MemoryInfo info) {
        this.name = new SimpleStringProperty(name);
        this.addr = new SimpleLongProperty(info.getAddress());
        this.size = new SimpleLongProperty(info.getSize());
        this.end = Bindings.add(addr, size);
        this.type = new SimpleObjectProperty<>(info.getType());
        this.access = new SimpleIntegerProperty(info.getPerm());
    }

    public SimpleStringProperty nameProperty(){
        return name;
    }

    public SimpleLongProperty addrProperty(){
        return addr;
    }

    public long getAddr(){
        return addr.get();
    }

    public SimpleLongProperty sizeProperty(){
        return size;
    }

    public NumberBinding endProperty(){
        return end;
    }

    public SimpleObjectProperty<MemoryType> typeProperty(){
        return type;
    }

    public SimpleIntegerProperty accessProperty(){
        return access;
    }

    public long getEnd() {
        return end.longValue();
    }
}
