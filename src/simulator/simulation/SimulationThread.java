package simulator.simulation;

public class SimulationThread extends Thread {

    private Simulation simulation;

    public SimulationThread(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public synchronized void start() {
        simulation.start();
        super.start();
    }

    @Override
    public void run() {
        System.out.println("Запуск потока");

//        ArrayList<Robot>  robots  = simulation.getRobots();
//        ArrayList<Target> targets = simulation.getTargets();

        while(simulation.nextIteration()) {
            try {
//                SwingUtilities.invokeAndWait(() -> {
//                    g.clearRect(0, 0, 1000, 700);
//                    for (Target target : targets)
//                        target.draw(g);
//                    for (Robot robot : robots) {
//                        robot.draw(g);
//                    }
//                });
                Thread.sleep(50);
                simulation.notifyObservers();
            } catch (Exception err) {
                err.printStackTrace();
            }
        }

        System.out.println("Завершение потока");
    }

}
