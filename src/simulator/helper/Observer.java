package simulator.helper;

public interface Observer {

    void update(Observable observable, SimulatorEvent event);
}
