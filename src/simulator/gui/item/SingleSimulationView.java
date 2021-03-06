package simulator.gui.item;

import simulator.gui.SimulationList;
import simulator.helper.Observable;
import simulator.helper.Observer;
import simulator.helper.SimulatorEvent;
import simulator.simulation.wrapper.SimulationWrapper;
import simulator.simulation.wrapper.SingleSimulation;

import javax.swing.*;
import java.awt.*;

public class SingleSimulationView extends JPanel implements Observer {

    private SimulationList parent;
    private JLabel lbl_status;

    public SingleSimulationView(SimulationList list, SimulationWrapper wrapper) {
        parent = list;

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setMaximumSize(new Dimension(170, 130));
        setPreferredSize(new Dimension(170, 130));
        setMinimumSize(new Dimension(170, 130));

        JLabel lbl_name = new JLabel(wrapper.getName());
        lbl_status = new JLabel("Ждет");

        JButton btn_start = new JButton("s");
        JButton btn_pause = new JButton("p");
        JButton btn_show  = new JButton("sh");
        JButton btn_restart = new JButton("re");

        // TODO: нормальные элементы для оберток
        if (!(wrapper instanceof SingleSimulation)) {
            btn_show.setEnabled(false);
            btn_restart.setEnabled(false);
        }

        add(lbl_name,   new GridBagConstraints(0, 0, 3, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        add(lbl_status, new GridBagConstraints(0, 1, 3, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

        add(btn_start,  new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        add(btn_pause,  new GridBagConstraints(1, 2, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        add(btn_show,   new GridBagConstraints(2, 2, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        add(btn_restart,new GridBagConstraints(0, 3, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

        btn_start.addActionListener(e -> parent.start(this));
        btn_pause.addActionListener(e -> parent.pause(this));
        btn_show.addActionListener (e -> parent.showSimulation(this));
        btn_restart.addActionListener(e -> wrapper.restart());
    }

    @Override
    public void update(Observable observable, SimulatorEvent event) {
        if (SimulatorEvent.isWrapperEvent(event)) {
            lbl_status.setText(event.toString());
        }
    }
}