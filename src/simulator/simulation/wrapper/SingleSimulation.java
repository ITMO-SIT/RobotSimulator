package simulator.simulation.wrapper;

import simulator.helper.Observable;
import simulator.helper.Observer;
import simulator.helper.SimulatorEvent;
import simulator.services.ClassStorage;
import simulator.simulation.Simulation;

import java.util.HashMap;

public class SingleSimulation extends SimulationWrapper {

    protected Simulation simulation;

    public SingleSimulation(HashMap<String, String> constParam) {
        name = "Симуляция";
        super.setParam(constParam);
        try {
            simulation = createSimulation();
            simulation.setSimulationParam(constParam);
            simulation.addObserver(this);
        } catch (Exception err) {
            System.out.println("ERROR: не удалось создать симуляцию.");
        } /*catch (ClassCastException e) {
            System.out.println("ERROR: cannot cast.");
        }*/
    }

    @Override
    public Simulation start() {
        switch (simulation.getStatus()) {
            case NOT_INITIALIZED:
                simulation.init();
            case INITIALIZED:
            case PAUSE:
                simulation.start();
                notifyObservers(SimulatorEvent.WRAPPER_WORK);
                return simulation;
            default:
                return null;
        }
    }

    @Override
    public void pause() {
        switch (simulation.getStatus()) {
            case RUNNING:
                simulation.pause();
                notifyObservers(SimulatorEvent.WRAPPER_PAUSE);
            default: break;
        }
    }

    @Override
    public Observable addSimulationObserver(Observer observer) {
        if (simulation != null)
            simulation.addObserver(observer);
        return simulation;
    }

    @Override
    public void update(Observable observable, SimulatorEvent event) {
        if (event == SimulatorEvent.SIMULATION_END) {
            observable.removeObserver(this);
            notifyObservers(SimulatorEvent.WRAPPER_END);
        }
    }
}
