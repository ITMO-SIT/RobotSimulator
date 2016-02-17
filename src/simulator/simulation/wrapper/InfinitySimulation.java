package simulator.simulation.wrapper;


import simulator.helper.Observable;
import simulator.helper.Observer;
import simulator.helper.SimulatorEvent;
import simulator.services.Configuration;
import simulator.simulation.Simulation;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedList;

public class InfinitySimulation extends SimulationWrapper {

    private boolean isActive;
    private HashMap<String, String> constParam;
    private LinkedList<Simulation> simulations;

    private FileWriter out;

    public InfinitySimulation(HashMap<String, String> constParam) {
        this.constParam = constParam;
        simulations = new LinkedList<>();
        isActive = false;

        if (constParam.get("name") != null)
            name = constParam.get("name");
        else
            name = "Серия симуляций";

        if (constParam.get("resultFilePath") != null) {
            try {
                out = new FileWriter(new File(constParam.get("resultFilePath")));
            } catch (Exception ignore) {}
        }

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
            Simulation simulation = Configuration.getInstance().newSimulationInstance();
            simulation.setSimulationParam(constParam);
            simulation.init();
            simulation.addObserver(this);
            simulation.start();
            notifyObservers(SimulatorEvent.WRAPPER_WORK);
            simulations.add(simulation);
            return simulation;
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
