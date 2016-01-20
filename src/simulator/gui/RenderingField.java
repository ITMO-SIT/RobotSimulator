package simulator.gui;

import simulator.helper.Observer;
import simulator.robot.Robot;
import simulator.simulation.Simulation;
import simulator.target.Target;


import java.awt.*;
import java.util.ArrayList;

public class RenderingField extends Canvas implements Observer {

    private ArrayList<Robot>  robots;
    private ArrayList<Target> targets;

    public RenderingField() {
        setBackground(Color.GRAY);
        robots  = new ArrayList<>(0);
        targets = new ArrayList<>(0);
    }

    @Override
    public void update() {
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        for (Target target : targets)
            target.draw(g);
        for (Robot robot : robots)
            robot.draw(g);
    }


    public void setSimulation(Simulation simulation) {
        robots  = simulation.getRobots();
        targets = simulation.getTargets();
    }
}
