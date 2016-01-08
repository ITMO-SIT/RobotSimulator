package simulator.simulation;

import simulator.configuration.Configuration;
import simulator.robot.Robot;
import simulator.target.Target;

public class TestSimulation extends Simulation {

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
        isActive = true;
    }

    @Override
    public boolean nextIteration() {
        if (!isActive) return false;
        boolean f = false;
        for (Robot robot : robots) {
            if (robot.isActive()) f = true;
            robot.doStep();
        }
        if (!f) isActive = false;
//        robots.forEach(Robot::doStep);
        return true;
    }
}
