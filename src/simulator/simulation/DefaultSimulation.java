package simulator.simulation;

import simulator.configuration.Configuration;
import simulator.robot.Robot;
import simulator.target.Target;

public class DefaultSimulation extends Simulation {

    private int count;

    @Override
    public void init() {
        System.out.println("Начало симуляции");

        Configuration conf = Configuration.getInstance();

        robots.clear();
        targets.clear();

        //---------------------------------------//
        Target target;
        target = conf.newTargetInstance();
        target.setX(10);
        target.setY(10);
        target.setSize(20);
        targets.add(target);

        target = conf.newTargetInstance();
        target.setX(10);
        target.setY(190);
        target.setSize(20);
        targets.add(target);

        target = conf.newTargetInstance();
        target.setX(150);
        target.setY(190);
        target.setSize(20);
        targets.add(target);

        target = conf.newTargetInstance();
        target.setX(125);
        target.setY(30);
        target.setSize(20);
        targets.add(target);
        //---------------------------------------//
        Robot robot;
        robot = conf.newRobotInstance();
        robot.setX(90);
        robot.setY(190);
        robot.setSpeed(5);
        robots.add(robot);

        robot = conf.newRobotInstance();
        robot.setX(70);
        robot.setY(90);
        robot.setSpeed(5);
        robots.add(robot);

        robot = conf.newRobotInstance();
        robot.setX(50);
        robot.setY(90);
        robot.setSpeed(5);
        robots.add(robot);

        robot = conf.newRobotInstance();
        robot.setX(100);
        robot.setY(90);
        robot.setSpeed(5);
        robots.add(robot);

        for (Robot r : robots) {
            r.setSimulation(this);
            targets.forEach(r::addTarget);
        }
        //---------------------------------------//

        count = 0;
        isActive = true;
    }

    @Override
    public boolean nextIteration() {
        if (!isActive) return false;
        if (count++ < 100) {
            robots.forEach(Robot::doStep);
            return true;
        }
        return false;
    }
}
