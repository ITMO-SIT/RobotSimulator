package simulator.simulation;

import simulator.configuration.Configuration;
import simulator.robot.Robot;
import simulator.target.Target;

import java.util.ArrayList;

public class DefaultSimulation implements Simulation {

    private ArrayList<Robot> robots;

    @Override
    public void execute() {
        System.out.println("Начало симуляции");

        Configuration conf = Configuration.getInstance();
        robots = conf.getRobots();

        Target target = conf.newTargetInstance();
        target.setX(10);
        target.setY(10);
        target.setSize(10);

        Robot robot = conf.newRobotInstance();
        robot.setX(90);
        robot.setY(90);
        robot.setSpeed(5);
        robot.addTarget(target);

        for (int i = 0; i < 25; i++)
            nextIteration();
        stop();
    }

    @Override
    public void stop() {
        System.out.println("конец симуляции");
    }

    @Override
    public void nextIteration() {
        robots.forEach(Robot::doStep);
    }
}
