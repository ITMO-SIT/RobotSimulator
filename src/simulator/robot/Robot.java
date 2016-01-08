package simulator.robot;

import simulator.simulation.Simulation;
import simulator.target.Target;

import java.awt.*;
import java.util.ArrayList;

abstract public class Robot {

    protected boolean    isActive;
    protected Simulation simulation;

    protected double x, y;
    protected double speed;
    protected ArrayList<Target> targets;

    abstract public void doStep();
    abstract public void targetDone();

    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillOval((int)x - 2, (int)y - 2, 4, 4);
    }

    public void addTarget(Target newTarget) {
        if (targets == null) targets = new ArrayList<>();
        targets.add(newTarget);
    }


    // ------- getter and setter region start -------//
    public double getX() {return x;}
    public double getY() {return y;}
    public double  getSpeed() {return speed;}
    public boolean isActive() {return isActive;}

    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public void setSpeed(double speed)    {this.speed = speed;}
    public void setActive(boolean active) {isActive = active;}
    public void setSimulation(Simulation simulation) {this.simulation = simulation;}
    // ------- getter and setter region end   -------//
}
