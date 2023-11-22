package them.mdbell.javafx.control;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;
import them.mdbell.util.HexUtils;
import them.mdbell.util.MemValueType;

public class MemValueSpinner extends Spinner<Long> {

    private MemValueType type = MemValueType.INT;

    private boolean decimalBase;

    public MemValueSpinner() {
        setValueFactory(new MemValueFactory());
        setType(MemValueType.INT);
        decimalBase = false;
    }

    public void setType(MemValueType type) {
        this.type = type;
        if (type.isFloat()) {
            MemValueFactory factory = (MemValueFactory)getValueFactory();
            factory.setValueFloat((double)factory.getValue());
        }
        getEditor().setText(getValueAsString());
    }

    public void setDecimalBase(boolean decimalBase) {
        this.decimalBase = decimalBase;
        getEditor().setText(getValueAsString());
    }

    public String getValueAsString() {
        return getValueFactory().getConverter().toString(getValue());
    }

    public String getValueAsHexString() {
        long value = getValueFactory().getValue();
        return "0x" + HexUtils.pad('0', type.getSize() * 2,
                Long.toUnsignedString(value, 16).toUpperCase()
        );
    }

    class MemValueFactory extends SpinnerValueFactory<Long> {
        private double floatValue;

        public MemValueFactory() {
            setValue(0L);
            valueProperty().addListener((observable, oldValue, newValue) -> setValue(newValue));

            setConverter(new StringConverter<>() {
                @Override
                public String toString(Long value) {
                    if (decimalBase) {
                        return switch (type) {
                            case DOUBLE -> String.format("%.8f", Double.longBitsToDouble(value));
                            case FLOAT -> String.format("%.4f", Float.intBitsToFloat(value.intValue()));
                            case BYTE -> String.valueOf(value.byteValue());
                            case SHORT -> String.valueOf(value.shortValue());
                            case INT -> String.valueOf(value.intValue());
                            default -> String.valueOf(value);
                        };
                    }

                    return "0x" + HexUtils.pad('0', type.getSize() * 2,
                        Long.toUnsignedString(value, 16).toUpperCase()
                    );
                }

                @Override
                public Long fromString(String string) {
                    // Trim and remove leading '0x' if present
                    String trimmed = (string.trim().toLowerCase().startsWith("0x")
                        ? string.trim().substring(2)
                        : string.trim()
                    );

                    // Try to parse string
                    try {
                        if (decimalBase) {
                            // Treat as decimal value
                            return (long)switch (type) {
                                case DOUBLE -> Double.parseDouble(trimmed);
                                case FLOAT -> Float.parseFloat(trimmed);
                                case BYTE -> Byte.parseByte(trimmed);
                                case SHORT -> Short.parseShort(trimmed);
                                case INT -> Integer.parseInt(trimmed);
                                default -> Long.parseUnsignedLong(trimmed);
                            };
                        }

                        // Treat as Hex value
                        return Long.parseUnsignedLong(trimmed, 16);
                    } catch (NumberFormatException e) {
                        // Parsing failed so just leave unchanged
                        return getValue();
                    }
                }
            });
        }

        public void setValueFloat(double value) {
            floatValue = value;
            if (type.isFloat()) {
                setValue(type.getSize() == 4
                    ? (long)Float.floatToIntBits((float)value)
                    : Double.doubleToLongBits(value)
                );
            } else {
                setValue((long)Math.floor(value + 0.5));
            }
        }

        @Override
        public void decrement(int steps) {
            if (type.isFloat()) {
                floatValue -= steps;
                setValueFloat(floatValue);
            } else {
                long l = getValue();
                l -= steps;
                setValue(l);
            }
        }

        @Override
        public void increment(int steps) {
            if (type.isFloat()) {
                floatValue += steps;
                setValueFloat(floatValue);
            } else {
                long l = getValue();
                l += steps;
                setValue(l);
            }
        }
    }
}
