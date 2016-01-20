package simulator.simulation;

import simulator.configuration.Configuration;
import simulator.robot.AnyLogicRobot;
import simulator.robot.Robot;
import simulator.target.Target;

import java.util.HashMap;
import java.util.Random;

public class AnyLogicSimulation extends Simulation<AnyLogicRobot> {

    double criticalDist = 5;        // минимальное расстояние
    double activeDist   = 20;       // расстоние взаимодействия

    @Override
    public void init() {
        robots.clear();
        targets.clear();

        Configuration conf = Configuration.getInstance();

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

        Random random = new Random(666);    // ГПСЧ для расстановки роботов

        int philistine = 300;   // количество обычных роботов
        int goodboy = 30;       // количество хороших роботов
        int enemy = 5;          // количество плохих роботов
        int N = philistine + goodboy + enemy;

        int cols = 20;
        int rows  = (int) Math.ceil((double) N / cols);

        for (int i = 0; i < N; i++) {
            int X, Y;
            while (true) {
                X = (int)(300 + random.nextInt(cols) * (4 + criticalDist));
                Y = (int)(400 + random.nextInt(rows) * (4 + criticalDist));
                if (!chekRobotXY(X, Y)) break;
            }
            AnyLogicRobot robot = (AnyLogicRobot) conf.newRobotInstance();
            robot.setCriticalDist(criticalDist);
            robot.setActiveDist(activeDist);
            robot.setX(X);
            robot.setY(Y);
            robot.setSpeed(1);
            robot.setSimulation(this);
            targets.forEach(robot::addTarget);
            robots.add(robot);
            if (i < philistine)
                robot.setRobotType(AnyLogicRobot.Type.philistine);
            else if (i < philistine + goodboy)
                robot.setRobotType(AnyLogicRobot.Type.goodboy);
            else
                robot.setRobotType(AnyLogicRobot.Type.enemy);
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
        return true;
    }

    @Override
    public void setSimulationParam(HashMap<String, String> param) {
        super.setSimulationParam(param);
        if (param.get("CriticalDist") != null)
            criticalDist = Integer.parseInt(param.get("CriticalDist"));
        if (param.get("ActiveDist") != null)
            activeDist = Integer.parseInt(param.get("ActiveDist"));
    }

    // только для инициализации. потом не будет
    private boolean chekRobotXY(int X, int Y) {
        for (Robot robot : robots)
            if (robot.getX() == X && robot.getY() == Y)
                return true;
        return false;
    }
}