package simulator.simulation;

public interface Simulation {

    void execute();
    void stop();
    boolean nextIteration();
}
