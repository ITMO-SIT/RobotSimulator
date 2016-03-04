package simulator.simulation.wrapper;


import simulator.helper.Observable;
import simulator.helper.Observer;
import simulator.helper.SimulatorEvent;
import simulator.helper.params.SimulationParam;
import simulator.simulation.Simulation;

import java.util.HashMap;
import java.util.LinkedList;

public class InfinitySimulation extends SimulationWrapper {

    private boolean isActive;
    private HashMap<String, SimulationParam> simParam;
    private LinkedList<Simulation> simulations;

    public InfinitySimulation(HashMap<String, SimulationParam> simParam) {
        this.simParam = simParam;
        simulations = new LinkedList<>();
        isActive = false;
        name = "<Бесконечная серия симуляций";
        super.setParam(simParam);
    }

    @Override
    public void pause() {
        isActive = false;
    }

    public void activateWrapper() {
        isActive = true;
    }

    @Override
    public Simulation start() {
        if (isActive) {
            try {
                Simulation simulation = createSimulation();
                simulation.setSimulationParam(simParam);
                simulation.init();
                simulation.addObserver(this);
                simulation.start();
                notifyObservers(SimulatorEvent.WRAPPER_WORK);
                simulations.add(simulation);
                return simulation;
            } catch (Exception ignore) {}
        }
        return null;
    }

    @Override
    public Observable addSimulationObserver(Observer observer) {
        return null;
    }

    @Override
    public void update(Observable observable, SimulatorEvent event) {
        if (event == SimulatorEvent.SIMULATION_END) {
            if (out != null) {
                try {
                    out.write(observable.toString());
                    out.append('\n');
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            observable.removeObserver(this);
            simulations.remove(observable);
            if (simulations.isEmpty()) notifyObservers(SimulatorEvent.WRAPPER_END);
        }
    }
}
