package simulator.simulation;

import simulator.helper.Observable;
import simulator.helper.Observer;
import simulator.field.Field;
import simulator.robot.Robot;
import simulator.target.Target;

import java.util.ArrayList;
import java.util.HashMap;

abstract public class Simulation<R extends Robot> implements Observable {
// ---------------------- поля класса ---------------------- //
    protected String  name;
    protected boolean isActive;

    protected ArrayList<R>      robots;
    protected ArrayList<Target> targets;
    protected Field field;

    protected ArrayList<Observer> observers;
// ---------------------- поля класса ---------------------- //
// ------------------- абстрактные методы ------------------ //
    abstract public void    init();
    abstract public boolean nextIteration();
// ------------------- абстрактные методы ------------------ //

    protected Simulation() {
        robots    = new ArrayList<>();
        targets   = new ArrayList<>();
        observers = new ArrayList<>();
        name = "Симуляция";
        isActive = false;
    }

    public void setSimulationParam(HashMap<String, String> param) {
        if (param.get("SimulationName") != null) name = param.get("SimulationName");
    }

    public void start() {isActive = true;}
    public void pause() {isActive = false;}

    public final ArrayList<R>  getRobots()  {return robots;}
    public final ArrayList<Target> getTargets() {return targets;}
    public final Field   getField() {return field;}
    public final boolean isActive() {return isActive;}
    public final String  getName()  {return name;}

// ------------- реализация интерфейса Observable ---------- //
    @Override
    public final void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public final void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public final void notifyObservers() {
        observers.forEach(Observer::update);
    }
// ------------- реализация интерфейса Observable ---------- //

}
