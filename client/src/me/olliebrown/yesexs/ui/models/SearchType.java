package me.olliebrown.yesexs.ui.models;

import them.mdbell.util.ILocalized;

public enum SearchType implements ILocalized {
    UNKNOWN("search.search_types.unk"),
    PREVIOUS("search.search_types.prev"),
    KNOWN("search.search_types.known"),
    DIFFERENT("search.search_types.diff");
    final String key;

    SearchType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public boolean requiresPrevious() {
        return this == PREVIOUS || this == DIFFERENT;
    }
}
