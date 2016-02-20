package simulator.simulation.ready;

import simulator.robot.Robot;
import simulator.simulation.Simulation;
import simulator.simulation.SimulationStatus;
import simulator.target.Target;

public class DefaultSimulation extends Simulation<Robot> {

    @Override
    public void init() {
        robots.clear();
        targets.clear();
        try {
            Target target = createTarget();
            target.setX(0);
            target.setY(0);
            target.setSize(100);
            targets.add(target);
        } catch (Exception ignore) {}

        try {
            Target target = createTarget();
            target.setX(800);
            target.setY(0);
            target.setSize(100);
            targets.add(target);
        } catch (Exception ignore) {}

        for (int i = 0; i < 20; i++)
            for (int j = 0; j < 15; j++) {
                try {
                    Robot robot = createRobot();
                    robot.setX(300 + i * 10);
                    robot.setY(400 + j * 10);
                    robot.setSpeed(2);
                    targets.forEach(robot::addTarget);
                } catch (Exception ignore) {}
            }
        status = SimulationStatus.INITIALIZED;
    }

    @Override
    public void nextIteration() {
        boolean f = false;
        for (Robot robot : robots) {
            if (robot.isActive()) f = true;
            robot.doStep();
        }
        if (!f) {
            status = SimulationStatus.ENDED;
        }
    }
}
