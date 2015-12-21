package simulator.simulation;

import simulator.configuration.Configuration;
import simulator.robot.Robot;
import simulator.target.Target;

import java.util.ArrayList;

public class DefaultSimulation implements Simulation {

    private ArrayList<Robot> robots;
    private int count;

    @Override
    public void execute() {
        System.out.println("Начало симуляции");

        Configuration conf = Configuration.getInstance();
        robots = conf.getRobots();

        //---------------------------------------//
        Target target;
        target = conf.newTargetInstance();
        target.setX(10);
        target.setY(10);
        target.setSize(20);

        target = conf.newTargetInstance();
        target.setX(10);
        target.setY(190);
        target.setSize(20);

        target = conf.newTargetInstance();
        target.setX(150);
        target.setY(190);
        target.setSize(20);

        target = conf.newTargetInstance();
        target.setX(125);
        target.setY(30);
        target.setSize(20);
        //---------------------------------------//
        Robot robot;
        robot = conf.newRobotInstance();
        robot.setX(90);
        robot.setY(190);
        robot.setSpeed(5);
        conf.getTargets().forEach(robot::addTarget);

        robot = conf.newRobotInstance();
        robot.setX(100);
        robot.setY(90);
        robot.setSpeed(5);
        conf.getTargets().forEach(robot::addTarget);
        //---------------------------------------//

        count = 0;
    }

    @Override
    public void stop() {
        System.out.println("конец симуляции");
    }

    @Override
    public boolean nextIteration() {
        if (count++ < 100) {
            robots.forEach(Robot::doStep);
            return true;
        }
        return false;
    }
}
