package them.mdbell.util;

public enum MemValueType implements ILocalized {
    BYTE("search.data_types.byte", 1),
    SHORT("search.data_types.short", 2),
    INT("search.data_types.int", 4),
    LONG("search.data_types.long", 8),
    FLOAT("search.data_types.float", 4),
    DOUBLE("search.data_types.double", 8);

    final String key;
    final int bytes;

    MemValueType (String key, int bytes) {
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

    public boolean isFloat() {
        return this == FLOAT || this == DOUBLE;
    }
}