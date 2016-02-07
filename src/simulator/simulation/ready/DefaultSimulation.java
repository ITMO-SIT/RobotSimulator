package simulator.simulation.ready;

import simulator.services.Configuration;
import simulator.robot.Robot;
import simulator.simulation.Simulation;
import simulator.simulation.SimulationStatus;
import simulator.target.Target;

public class DefaultSimulation extends Simulation<Robot> {

    @Override
    public void init() {
        Configuration conf = Configuration.getInstance();

        robots.clear();
        targets.clear();

        Target target = conf.newTargetInstance();
        target.setX(0);
        target.setY(0);
        target.setSize(100);
        targets.add(target);

        target = conf.newTargetInstance();
        target.setX(800);
        target.setY(0);
        target.setSize(100);
        targets.add(target);

        for (int i = 0; i < 20; i++)
            for (int j = 0; j < 15; j++) {
                Robot robot = conf.newRobotInstance();
                robot.setX(300 + i * 10);
                robot.setY(400 + j * 10);
                robot.setSpeed(2);
                targets.forEach(robot::addTarget);
                robots.add(robot);
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
