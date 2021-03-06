package simulator.helper.params;

import java.util.Random;

public class RandomParam extends SimulationParam {

    protected Object minValue;
    protected Object maxValue;

    public RandomParam(ValueType valueType, Object min, Object max) {
        this.valueType = valueType;
        minValue = min;
        maxValue = max;
    }

    // Т_Т
    @Override @SuppressWarnings("unchecked")
    public <T> T getValue() {
        Random random = new Random();
        switch (valueType) {
            case INT : return (T) (Integer.valueOf(random.nextInt((Integer) maxValue - (Integer) minValue) + (Integer) minValue));
            case LONG: return (T) (Long.valueOf((long)(random.nextDouble() * ((Long) maxValue - (Long) minValue) + (Long) minValue)));
            case FLOAT:  return (T) (Float.valueOf(random.nextFloat() * ((Float) maxValue - (Float) minValue) + (Float) minValue));
            case DOUBLE: return (T) (Double.valueOf(random.nextDouble() * ((Double) maxValue - (Double) minValue) + (Double) minValue));
        }
        return null;
    }
}
