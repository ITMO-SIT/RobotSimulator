package simulator.gui;

import simulator.configuration.Configuration;
import simulator.robot.Robot;
import simulator.simulation.Simulation;
import simulator.target.Target;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SimulationThread extends Thread {

    private Graphics2D g;

    public SimulationThread(Graphics2D g) {
        this.g = g;
    }

    @Override
    public synchronized void start() {
        Configuration.getInstance().newSimulationInstance().execute();
        super.start();
    }

    public void continueSimulation() {
        Configuration.getInstance().getSimulation().continueSimulation();
        super.start();
    }

    @Override
    public void run() {
        Configuration config = Configuration.getInstance();
        ArrayList<Robot> robots = config.getRobots();
        Simulation sim = config.getSimulation();

        while(sim.nextIteration()) {
            g.clearRect(0, 0, 400, 400);
            for (Target target : config.getTargets())
                target.draw(g);
            try {
                SwingUtilities.invokeAndWait(() -> {
                    for (Robot robot : robots) {
                        robot.draw(g);
                    }
                });
                Thread.sleep(500);
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }

}
