package simulator.helper.params;

public class ConstParam extends SimulationParam{

    protected Object value;

    public ConstParam(Object value) {
        this.value = value;
    }

    @Override @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }
}
