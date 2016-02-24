package simulator.gui;

import simulator.helper.Observable;
import simulator.helper.Observer;
import simulator.helper.SimulatorEvent;
import simulator.robot.Robot;
import simulator.simulation.Simulation;
import simulator.target.Target;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RenderingField extends JPanel implements Observer {

    private ArrayList<Robot>  robots;
    private ArrayList<Target> targets;
    private Simulation simulation;

    public RenderingField() {
        setBackground(Color.GRAY);
        robots  = new ArrayList<>(0);
        targets = new ArrayList<>(0);
    }

    @Override
    public void update(Observable o, SimulatorEvent event) {
        if (event == SimulatorEvent.SIMULATION_STEP_END)
            repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (Target target : targets)
            target.draw(g);
        for (Robot robot : robots)
            robot.draw(g);
    }


    public void setSimulation(Simulation sim) {
        if (sim == null) return;
        if (simulation != null) {
            simulation.removeObserver(this);
            simulation.setDelay(0);
        }
        simulation = sim;
        simulation.setDelay(25);
        robots  = simulation.getRobots();
        targets = simulation.getTargets();
    }
}
