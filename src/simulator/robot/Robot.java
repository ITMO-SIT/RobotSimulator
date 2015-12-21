package simulator.robot;

import simulator.target.Target;

import java.awt.*;
import java.util.ArrayList;

abstract public class Robot {
    protected double x, y;
    protected double speed;
    protected ArrayList<Target> targets;
    protected boolean isActive;

    abstract public void doStep();
    abstract public void targetDone();
    abstract public void whoNear();


    public void draw(Graphics2D g) {
        g.setColor(Color.ORANGE);
        g.fillOval((int)x - 5, (int)y - 5, 10, 10);
    }

    public void addTarget(Target newTarget) {
        if (targets == null) targets = new ArrayList<>();
        System.out.println("add target");
        targets.add(newTarget);
    }

    // ------- getter and setter region start -------//
    public double getX() {return x;}
    public double getY() {return y;}
    public double getSpeed() {return speed;}

    public void setX(double x) {this.x = x;}
    public void setY(double y) {this.y = y;}
    public void setSpeed(double speed) {this.speed = speed;}
    // ------- getter and setter region end   -------//
}
