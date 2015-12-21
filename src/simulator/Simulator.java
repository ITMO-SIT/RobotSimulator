package simulator;

import simulator.configuration.Configuration;
import simulator.robot.Robot;
import simulator.simulation.Simulation;
import simulator.target.Target;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public final class Simulator extends JFrame{

    public Simulator() {
        setSize(new Dimension(500, 500));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JButton btn_execute = new JButton("Execute Simulation");
        Canvas  canvas = new Canvas();

        btn_execute.addActionListener(e -> {
            System.out.println("Роботы поработят мир!!!");
            new Thread(() -> {
                Configuration config = new Configuration(new File("configuration.txt"));
                Simulation sim = config.newSimulationInstance();
                sim.execute();
                ArrayList<Robot> robots = config.getRobots();
                Graphics2D g = (Graphics2D) canvas.getGraphics();
                while(sim.nextIteration()) {
                    g.clearRect(0, 0, 1000, 1000);
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
                sim.stop();
            }).start();
        });


        add(btn_execute, BorderLayout.NORTH);
        add(canvas);

        setVisible(true);
    }

    public static void main(String[] args) {
        new Simulator();
//        System.out.println("Роботы поработят мир!!!");
//        Configuration config = new Configuration(new File("configuration.txt"));
//        config.newSimulationInstance().execute();
    }

}
