package simulator.gui;

import simulator.gui.item.SingleSimulationView;
import simulator.services.SimulationController;
import simulator.simulation.Simulation;
import simulator.simulation.wrapper.SimulationWrapper;

import javax.swing.*;
import java.awt.*;

public class SimulationList extends JPanel {

    private final SimulationController controller = new SimulationController();
    private RenderingField canvas;

    public SimulationList(RenderingField canvas) {
        this.canvas = canvas;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (SimulationWrapper wrapper : controller.getWrappers()) {
            SingleSimulationView view = new SingleSimulationView(this, wrapper);
            wrapper.addObserver(view);
            add(view);
        }

    }

    public void start(Component component) {
       controller.start(find(component));
    }

    public void pause(Component component) {
        controller.pause(find(component));
    }

    public void showSimulation(Component component) {
        canvas.setSimulation((Simulation)controller.addObserver(find(component), canvas));
        selectItem(component);
    }

    public void selectItem(Component selectedItem) {
        for (Component cmp : getComponents())
            cmp.setBackground(Color.WHITE);
        selectedItem.setBackground(Color.LIGHT_GRAY);
    }


    private int find(Component cmp) {
        Component[] cmps = getComponents();
        for (int i = 0; i < cmps.length; i++)
            if (cmps[i] == cmp) return i;
        return -1;
    }

}
