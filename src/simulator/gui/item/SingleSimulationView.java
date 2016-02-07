package simulator.gui.item;

import com.sun.istack.internal.NotNull;
import simulator.gui.SimulationList;
import simulator.helper.Observable;
import simulator.helper.Observer;
import simulator.helper.SimulatorEvent;
import simulator.simulation.wrapper.SimulationWrapper;

import javax.swing.*;
import java.awt.*;

public class SingleSimulationView extends JPanel implements Observer {

    private SimulationWrapper wrapper;

    private SimulationList parent;
    private JLabel lbl_status;

    public SingleSimulationView(SimulationList list, SimulationWrapper wrapper) {
        parent = list;
        this.wrapper = wrapper;

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setMaximumSize(new Dimension(175, 100));
        setPreferredSize(new Dimension(175, 100));
        setMinimumSize(new Dimension(175, 100));

        JLabel lbl_name = new JLabel(wrapper.getName());
        lbl_status = new JLabel("Ждет");

        JButton btn_start = new JButton("s");
        JButton btn_pause = new JButton("p");
        JButton btn_show  = new JButton("sh");

        add(lbl_name,   new GridBagConstraints(0, 0, 3, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        add(lbl_status, new GridBagConstraints(0, 1, 3, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        add(btn_start,  new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        add(btn_pause,  new GridBagConstraints(1, 2, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        add(btn_show,   new GridBagConstraints(2, 2, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

        btn_start.addActionListener(e -> parent.start(this));
        btn_pause.addActionListener(e -> parent.pause(this));
        btn_show.addActionListener (e -> parent.showSimulation(this));
    }

    @Override
    public void update(Observable observable, SimulatorEvent event) {
        if (SimulatorEvent.isWrapperEvent(event)) {
            lbl_status.setText(event.toString());
        }
    }
}