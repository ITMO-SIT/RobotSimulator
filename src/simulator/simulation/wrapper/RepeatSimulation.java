package simulator.simulation.wrapper;

import simulator.helper.Observable;
import simulator.helper.Observer;
import simulator.helper.SimulatorEvent;
import simulator.helper.params.SimulationParam;
import simulator.simulation.Simulation;

import java.util.HashMap;
import java.util.LinkedList;

public class RepeatSimulation extends SimulationWrapper {

    private int count;
    private HashMap<String, SimulationParam> simParam;
    private LinkedList<Simulation> simulations;

    public RepeatSimulation(HashMap<String, SimulationParam> simParam) {
        this.simParam = simParam;
        simulations = new LinkedList<>();
        name = "Серия симуляций";
        super.setParam(simParam);

        if (simParam.get("reps") == null) count = 25;
        else {
            count = simParam.get("reps").getValue();
            // TODO: удаление параметра reps из constParam, после написания класса MutableParam
        }
    }

    @Override
    public void restart() {

    }

    @Override
    public void pause() {
//        simulations.forEach(Simulation::pause);
    }

    @Override
    public Simulation start() {
        if (count != 0) {
            try {
                Simulation simulation = createSimulation();
                simulation.setSimulationParam(simParam);
                simulation.init();
                simulation.addObserver(this);
                simulation.start();
                notifyObservers(SimulatorEvent.WRAPPER_WORK);
                simulations.add(simulation);

                count--;
                System.out.println(name + " осталось " + count);
                return simulation;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Observable addSimulationObserver(Observer observer) {
//        if (simulations.isEmpty()) return null;
//        return simulations.getFirst();
        return null;
    }

    @Override
    public synchronized void update(Observable observable, SimulatorEvent event) {
        if (event == SimulatorEvent.SIMULATION_END) {
            if (out != null) {
                try {
                    out.write(observable.toString());
                    out.append('\n');
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            observable.removeObserver(this);
            simulations.remove(observable);
            if (simulations.isEmpty()) notifyObservers(SimulatorEvent.WRAPPER_END);
        }
    }
}
