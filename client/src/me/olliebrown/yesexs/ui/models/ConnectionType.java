package me.olliebrown.yesexs.ui.models;

import them.mdbell.util.ILocalized;

public enum ConnectionType implements ILocalized {

    USB("main.conn.usb"), NETWORK("main.conn.network");

    private final String key;

    ConnectionType(String key){
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
