package simulator.simulation.wrapper;

import simulator.helper.Observable;
import simulator.helper.Observer;
import simulator.helper.SimulatorEvent;
import simulator.helper.params.SimulationParam;
import simulator.services.ClassStorage;
import simulator.simulation.Simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

abstract public class SimulationWrapper implements Observable, Observer {

    protected String name;
    protected FileWriter out;

    private List<Observer> observers;
    private String pathSimulationClass;

    protected SimulationWrapper() {
        observers = new LinkedList<>();
    }

    abstract public void pause();
    abstract public Simulation start();
    abstract public Observable addSimulationObserver(Observer observer);

    public void setParam(HashMap<String, SimulationParam> param) {
        if (param.get("name") != null) name = param.get("name").getValue();
        if (param.get("simulation") != null) pathSimulationClass = param.get("simulation").getValue();
        if (param.get("resultFilePath") != null) {
            try {
                out = new FileWriter(new File(param.get("resultFilePath").<String>getValue()));
            } catch (IOException err) {
                System.err.println(err.getMessage());
            }
        }
    }

    // TODO: обработка исключений
    protected final Simulation createSimulation() throws Exception {
        return ClassStorage.getInstance().<Simulation>newClassInstance(pathSimulationClass);
    }

    public final String getName() {return name;}

    @Override
    public final void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public final void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public final void notifyObservers(SimulatorEvent event) {
        for (Observer observer : observers)
            observer.update(this, event);
    }
}
