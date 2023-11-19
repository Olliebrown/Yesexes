package me.olliebrown.yesexs.ui.models;

import them.mdbell.util.ILocalized;

public enum DataType implements ILocalized {
    BYTE("search.data_types.byte", 1),
    SHORT("search.data_types.short", 2),
    INT("search.data_types.int", 4),
    LONG("search.data_types.long", 8),
    FLOAT("search.data_types.float", 4),
    DOUBLE("search.data_types.double", 8),
    VECTOR("search.data_types.vector", 12),
    MATRIX("search.data_types.matrix", 48);

    final String key;
    final int bytes;

    DataType(String key, int bytes) {
        this.key = key;
        this.bytes = bytes;
    }

    @Override
    public String getKey() {
        return key;
    }

    public int getSize() {
        return bytes;
    }
}