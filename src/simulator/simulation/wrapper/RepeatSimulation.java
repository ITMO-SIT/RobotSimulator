package simulator.simulation.wrapper;


import simulator.helper.Observable;
import simulator.helper.Observer;
import simulator.helper.SimulatorEvent;
import simulator.services.Configuration;
import simulator.simulation.Simulation;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class RepeatSimulation extends SimulationWrapper {

    private int count;
    private HashMap<String, String> constParam;
    private LinkedList<Simulation> simulations;

    private FileWriter out;           // TODO: заменить на ResultController

    public RepeatSimulation(HashMap<String, String> constParam) {
        this.constParam = constParam;

        if (constParam.get("name") != null)
            name = constParam.get("name");
        else
            name = "Серия симуляций";

        if (constParam.get("reps") == null) count = 25;
        else {
            count = Integer.parseInt(constParam.get("reps"));
            // TODO: удаление параметра reps из constParam, после написания класса MutableParam
        }

        if (constParam.get("resultFilePath") != null) {
            try {
                out = new FileWriter(new File(constParam.get("resultFilePath")));
            } catch (Exception ignore) {}
        }


        simulations = new LinkedList<>();
    }

    @Override
    public void pause() {
//        simulations.forEach(Simulation::pause);
    }

    @Override
    public Simulation start() {
        if (count != 0) {
            count--;
            System.out.println(name + " осталось " + count);
            Simulation simulation = Configuration.getInstance().newSimulationInstance();
            simulation.setSimulationParam(constParam);
            simulation.init();
            simulation.addObserver(this);
            simulation.start();
            notifyObservers(SimulatorEvent.WRAPPER_WORK);
            simulations.add(simulation);
            return simulation;
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
//                    System.out.println(observable.toString());
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
