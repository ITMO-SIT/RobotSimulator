package simulator.simulation;

import org.w3c.dom.Node;
import simulator.Observable;
import simulator.Observer;
import simulator.field.Field;
import simulator.robot.Robot;
import simulator.target.Target;

import java.util.ArrayList;

abstract public class Simulation implements Observable {

    protected String  name;
    protected boolean isActive;

    protected ArrayList<Robot>  robots;
    protected ArrayList<Target> targets;
    protected Field field;

    protected Node initConf;

    protected ArrayList<Observer> observers;

    public Simulation() {
        robots  = new ArrayList<>();
        targets = new ArrayList<>();
        observers = new ArrayList<>();
        name = "Симуляция";
        isActive = false;
    }

    abstract public void    init();
    abstract public boolean nextIteration();

    /*abstract */public void start() {isActive = true;}
    /*abstract */public void pause() {isActive = false;}


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


    public void setInitConf(Node conf) {
        initConf = conf;
        Node temp = conf.getAttributes().getNamedItem("name");
        if (temp != null)
            name = temp.getNodeValue();
    }

    public final ArrayList<Robot>  getRobots()  {return robots;}
    public final ArrayList<Target> getTargets() {return targets;}
    public final Field   getField() {return field;}
    public final boolean isActive() {return isActive;}
    public final String  getName()  {return name;}
}
