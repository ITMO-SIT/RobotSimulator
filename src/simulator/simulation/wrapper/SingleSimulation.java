package simulator.simulation.wrapper;

import simulator.helper.Observable;
import simulator.helper.Observer;
import simulator.helper.SimulatorEvent;
import simulator.helper.params.SimulationParam;
import simulator.services.ClassStorage;
import simulator.simulation.Simulation;

import java.util.HashMap;

public class SingleSimulation extends SimulationWrapper {

    protected Simulation simulation;
    protected HashMap<String, SimulationParam> param;

    public SingleSimulation(HashMap<String, SimulationParam> param) {
        name = "Симуляция";
        super.setParam(param);
        this.param = param;
        try {
            simulation = createSimulation();
            simulation.setSimulationParam(param);
            simulation.addObserver(this);
        } catch (ClassCastException e) {
            System.out.println("ERROR: cannot cast.");
        } catch (Exception err) {
            System.out.println("ERROR: не удалось создать симуляцию.");
        }
    }

    @Override
    public void restart() {
        simulation.setSimulationParam(param);
        simulation.init();
        simulation.notifyObservers(SimulatorEvent.SIMULATION_STEP_END);
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
            System.out.printf(observable.toString());
        }
    }
}
