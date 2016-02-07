package simulator.services;


import simulator.helper.Observable;
import simulator.helper.Observer;
import simulator.helper.SimulatorEvent;
import simulator.simulation.Simulation;
import simulator.simulation.wrapper.SimulationWrapper;

import java.util.LinkedList;

public class ThreadController implements Observer {

    private int numberStreams;
    private LinkedList<SimulationWrapper> waite_queue;
    private LinkedList<Simulation>        running_queue;

    public ThreadController() {
        numberStreams = Runtime.getRuntime().availableProcessors();
        waite_queue   = new LinkedList<>();
        running_queue = new LinkedList<>();
    }

    public void add(SimulationWrapper wrapper) {
        waite_queue.addLast(wrapper);
        if (running_queue.size() < numberStreams)
            startTread();
    }

    private void startTread() {
        while (!waite_queue.isEmpty() && running_queue.size() < 4) {
            Simulation sim = waite_queue.getFirst().start();
            if (sim == null) {
                waite_queue.removeFirst();
            } else {
                running_queue.addLast(sim);
                sim.addObserver(this);
                new Thread(sim).start();
            }
        }
    }

    @Override
    public void update(Observable o, SimulatorEvent event) {
        if (event == SimulatorEvent.SIMULATION_END || event == SimulatorEvent.SIMULATION_PAUSE) {
            o.removeObserver(this);
            running_queue.remove(o);
            startTread();
        }
    }
}
