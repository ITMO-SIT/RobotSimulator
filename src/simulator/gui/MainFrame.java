package simulator.gui;

import simulator.simulation.Simulation;
import simulator.simulation.SimulationThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {

    private Simulation currentSimulation;

    public MainFrame() {
        setSize(new Dimension(1000, 700));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JButton btn_start   = new JButton("Start");
        JButton btn_pause   = new JButton("Pause");
        JButton btn_restart = new JButton("restart");
        final SimulationList simList = new SimulationList();
        final RenderingField canvas = new RenderingField();

        simList.addItemMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                SimulationListItem item = (SimulationListItem)e.getComponent();
                Simulation sim = item.getSimulation();
                if (currentSimulation != null)
                    currentSimulation.removeObserver(canvas);
                currentSimulation = sim;
                sim.addObserver(canvas);
                canvas.setSimulation(sim);
                simList.selectItem(item);
            }
        });

        btn_start.addActionListener(e -> (new SimulationThread(currentSimulation)).start());
        btn_pause.addActionListener(e -> currentSimulation.pause());
        btn_restart.addActionListener(e-> {
            currentSimulation.init();
            
        });

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolBar.add(btn_start);
        toolBar.add(btn_pause);
        toolBar.add(btn_restart);

        add(toolBar, BorderLayout.NORTH);
        add(simList, BorderLayout.EAST);
        add(canvas);

        setVisible(true);
    }
}
