package me.olliebrown.yesexs.ui.models;

import them.mdbell.util.ILocalized;

public enum RangeType implements ILocalized {
    ALL("search.range_types.all"),
    RANGE("search.range_types.range"),
    HEAP("search.range_types.heap"),
    TLS("search.range_types.thread");

    final String key;

    RangeType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
