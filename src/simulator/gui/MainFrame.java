package simulator.gui;

import simulator.configuration.Configuration;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private SimulationThread simulationThread;

    public MainFrame() {
        setSize(new Dimension(500, 500));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JButton btn_execute  = new JButton("Execute");
        JButton btn_stop     = new JButton("Stop");
        JButton btn_continue = new JButton("Continue");
        JButton btn_pause    = new JButton("Pause");
        Canvas canvas = new Canvas();

        btn_execute.addActionListener(e -> {
            simulationThread = new SimulationThread((Graphics2D) canvas.getGraphics());
            simulationThread.start();
        });
        btn_stop.addActionListener(e -> {
            if (simulationThread != null && simulationThread.isAlive()) {
                Configuration.getInstance().getSimulation().stop();
            }
        });
        btn_continue.addActionListener(e -> {
            if (simulationThread != null && !simulationThread.isAlive()) {
                simulationThread = new SimulationThread((Graphics2D) canvas.getGraphics());
                simulationThread.continueSimulation();
                Configuration.getInstance().getSimulation().continueSimulation();
            }
        });
        btn_pause.addActionListener(e -> {
            if (simulationThread != null && simulationThread.isAlive()) {
                Configuration.getInstance().getSimulation().pause();
            }
        });

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolBar.add(btn_execute);
        toolBar.add(btn_stop);
        toolBar.add(btn_continue);
        toolBar.add(btn_pause);
        add(toolBar, BorderLayout.NORTH);
        add(canvas);

        setVisible(true);
    }
}
