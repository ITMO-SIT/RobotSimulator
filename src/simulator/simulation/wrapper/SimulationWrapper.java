package simulator.simulation.wrapper;

import simulator.helper.Observable;
import simulator.helper.Observer;
import simulator.helper.SimulatorEvent;
import simulator.simulation.Simulation;

import java.util.LinkedList;
import java.util.List;

abstract public class SimulationWrapper implements Observable, Observer {

    protected String name;
    private List<Observer> observers;

    protected SimulationWrapper() {
        observers = new LinkedList<>();
    }

    abstract public void pause();
    abstract public Simulation start();
    abstract public Observable addSimulationObserver(Observer observer);

    public String getName() {return name;}

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(SimulatorEvent event) {
        for (Observer observer : observers)
            observer.update(this, event);
    }
}
