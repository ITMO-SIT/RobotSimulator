package simulator.helper.params;

public class MutableParam extends SimulationParam {

    protected Object minValue;
    protected Object maxValue;
    protected Object step;
    protected Object currentValue;

    public MutableParam(ValueType valueType, Object min, Object max, Object step) {
        this.valueType = valueType;
        this.minValue = this.currentValue = min;
        this.maxValue = max;
        this.step = step;
    }

    @Override @SuppressWarnings("unchecked")
    public <T> T getValue() {
        switch (valueType) {
            case INT: {
                int tmp = (Integer) currentValue + (Integer) step;
                if (tmp > (Integer) maxValue)
                    tmp = (Integer) minValue;
                return (T) (currentValue = tmp);
            }
            case LONG: {
                long tmp = (Long) currentValue + (Long) step;
                if (tmp > (Long) maxValue)
                    tmp = (Long) minValue;
                return (T) (currentValue = tmp);
            }
            case FLOAT: {
                float tmp = (Float) currentValue + (Float) step;
                if (tmp > (Float) maxValue)
                    tmp = (Float) minValue;
                return (T) (currentValue = tmp);
            }
            case DOUBLE: {
                double tmp = (Double) currentValue + (Double) step;
                if (tmp > (Double) maxValue)
                    tmp = (Double) minValue;
                return (T) (currentValue = tmp);
            }
        }
        return null;
    }
}
