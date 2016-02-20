package simulator.simulation.wrapper;

import simulator.helper.Observable;
import simulator.helper.Observer;
import simulator.helper.SimulatorEvent;
import simulator.simulation.Simulation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

abstract public class SimulationWrapper implements Observable, Observer {

    protected String name;
    protected String pathSimulation;

    private List<Observer> observers;

    protected SimulationWrapper() {
        observers = new LinkedList<>();
    }

    abstract public void pause();
    abstract public Simulation start();
    abstract public Observable addSimulationObserver(Observer observer);

    public void setParam(HashMap<String, String> param) {
        if (param.get("name") != null) name = param.get("name");
        if (param.get("simulation") != null) pathSimulation = param.get("simulation");
    }

    public String getName() {return name;}

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
