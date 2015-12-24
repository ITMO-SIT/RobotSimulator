package simulator.simulation;

abstract public class Simulation {

    protected boolean isActive;

    abstract public void execute();
    abstract public void stop();
    abstract public void continueSimulation();
    abstract public void pause();
    abstract public boolean nextIteration();
}
