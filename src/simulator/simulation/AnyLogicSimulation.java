package simulator.simulation;

import simulator.configuration.Configuration;
import simulator.robot.AnyLogicRobot;
import simulator.robot.Robot;
import simulator.target.Target;

import java.util.Random;

public class AnyLogicSimulation extends Simulation{

    // только для инициализации. потом не будет
    private boolean chekRobotXY(int X, int Y) {
        for (Robot robot : robots)
            if (robot.getX() == X && robot.getY() == Y)
                return true;
        return false;
    }

    @Override
    public void init() {

        double criticalDist = 5;        // минимальное расстояние
        double activeDist   = 20;       // расстоние взаимодействия

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

        Random random = new Random(666);

        int philistine = 300;  // количество обычных роботов
        int goodboy = 30;      // количество хороших роботов
        int enemy = 5;         // количество плохих роботов
        int N = philistine + goodboy + enemy;

        for (int i = 0; i < N; i++) {
            int X, Y;
            while (true) {
                X = (int)(300 + random.nextInt(20) * (4 + criticalDist));
                Y = (int)(400 + random.nextInt(17) * (4 + criticalDist));
                if (!chekRobotXY(X, Y)) break;
            }
            Robot robot = conf.newRobotInstance();
            robot.setX(X);
            robot.setY(Y);
            robot.setSpeed(1);
            robot.setSimulation(this);
            targets.forEach(robot::addTarget);
            robots.add(robot);
            if (robot instanceof AnyLogicRobot) {       // костыль, но пока так
                if (i < philistine)
                    ((AnyLogicRobot) robot).setRobotType(AnyLogicRobot.Type.philistine);
                else if (i < philistine + goodboy)
                    ((AnyLogicRobot) robot).setRobotType(AnyLogicRobot.Type.goodboy);
                else
                    ((AnyLogicRobot) robot).setRobotType(AnyLogicRobot.Type.enemy);
                ((AnyLogicRobot) robot).setCriticalDist(criticalDist);
                ((AnyLogicRobot) robot).setActiveDist(activeDist);
            } else {
                System.out.println("Неприменимый тип. Симуляция не инициализированна до конца!");
                System.out.println(robot.getClass().getName());
                return;
            }
        }
        isActive = true;
    }

    @Override
    public boolean nextIteration() {
        if (!isActive) {
            System.out.println(name + " закончена или приостановленна.");
            System.out.println("К цели 1 пришло " + targets.get(0).toString());
            System.out.println("К цели 2 пришло " + targets.get(1).toString());
            System.out.println("-------------------------------------");
            return false;
        }
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
