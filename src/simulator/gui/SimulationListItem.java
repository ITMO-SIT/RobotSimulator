package simulator.gui;

import simulator.helper.Observer;
import simulator.simulation.Simulation;

import javax.swing.*;
import java.awt.*;

public class SimulationListItem extends JPanel implements Observer {

    private Simulation simulation;

    private JLabel lbl_name;
    private JLabel lbl_status;

    public SimulationListItem(Simulation simulation) {
        this.simulation = simulation;
        simulation.addObserver(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setMaximumSize(new Dimension(150, 75));

        lbl_name   = new JLabel();
        lbl_status = new JLabel();

        lbl_name.setText(simulation.getName());
        lbl_status.setText("симуляция не работает");

        add(lbl_name);
        add(lbl_status);
    }

    @Override
    public void update() {
        if (simulation.isActive()) {
            lbl_status.setText("симуляция работает");
        } else {
            lbl_status.setText("симуляция не работает");
        }
    }

    public Simulation getSimulation() {return simulation;}
}
