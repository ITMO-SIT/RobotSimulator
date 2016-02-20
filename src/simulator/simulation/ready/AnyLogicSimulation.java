package simulator.simulation.ready;

import simulator.helper.InputSimulationParam;
import simulator.robot.AnyLogicRobot;
import simulator.robot.Robot;
import simulator.simulation.Simulation;
import simulator.simulation.SimulationStatus;
import simulator.target.Target;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class AnyLogicSimulation extends Simulation<AnyLogicRobot> {

    @InputSimulationParam private double criticalDist = 5;        // минимальное расстояние
    @InputSimulationParam private double activeDist   = 20;       // расстоние взаимодействия

    @InputSimulationParam private int countPhilistine = 300;   // количество обычных роботов
    @InputSimulationParam private int countGoodBoy = 30;       // количество хороших роботов
    @InputSimulationParam private int countEnemy = 5;          // количество плохих роботов

    @InputSimulationParam private double confidenceGoodBoy = 0.1;
    @InputSimulationParam private double distBetweenRobot = criticalDist;

    @InputSimulationParam private Integer positionSeed;
    @InputSimulationParam private Integer roboInitSeed;

    @Override
    public void init() {
        robots.clear();
        targets.clear();

        try {
            Target target = createTarget();
            target.setX(0);
            target.setY(0);
            target.setSize(100);
        } catch (Exception ignore) {}
        try {
            Target target = createTarget();
            target.setX(800);
            target.setY(0);
            target.setSize(100);
        } catch (Exception ignore) {}

        // верхний костыль. Хорошая цель
        try {
            Target target = createTarget();
            target.setX(100);
            target.setY(0);
            target.setSize(400, 5);
            target.setColor(new Color(50, 250, 50, 150));
        } catch (Exception ignore) {}
        // верхний костыль. Плохая цель
        try {
            Target target = createTarget();
            target.setX(500);
            target.setY(0);
            target.setSize(500, 5);
        } catch (Exception ignore) {}
        // боковой костыль. Хорошая цель
        try {
            Target target = createTarget();
            target.setX(0);
            target.setY(100);
            target.setSize(5, 200);
            target.setColor(new Color(50, 250, 50, 150));
        } catch (Exception ignore) {}
        // боковой костыль. Плохая цель
        try {
            Target target = createTarget();
            target.setX(995);
            target.setY(100);
            target.setSize(5, 200);
        } catch (Exception ignore) {}


        int N = countPhilistine + countGoodBoy + countEnemy;

        int cols = 20;
        int rows  = (int) Math.ceil((double) N / cols);

        // TODO: Random с getSeed(). А то костыль ужвсный глаз мозолит
        // TODO: Random с возможностью задать диапазон с нижней границе
        Random positionRandom;
        Random roboInitRandom;
        if (positionSeed != null) positionRandom = new Random(positionSeed);
        else {
            positionRandom = new Random();
            positionSeed = positionRandom.nextInt();
            positionRandom.setSeed(positionSeed);
        }
        if (roboInitSeed != null) roboInitRandom = new Random(roboInitSeed);
        else {
            roboInitRandom = new Random();
            roboInitSeed = roboInitRandom.nextInt();
            roboInitRandom.setSeed(roboInitSeed);
        }

        int N = countPhilistine + countGoodBoy + countEnemy;
        List<AnyLogicRobot> robots = new LinkedList<>();
        try {
            robots = createRobot(N);
        } catch (Exception ignore) {}

        int cols = 20;
        int rows  = (int) Math.ceil((double) N / cols);

        int i = 0;
        for (AnyLogicRobot robot : robots) {
            int X, Y;
            while (true) {
                X = (int)(300 + positionRandom.nextInt(cols) * (4 + distBetweenRobot));
                Y = (int)(400 + positionRandom.nextInt(rows) * (4 + distBetweenRobot));
                if (!checkRobotXY(X, Y)) break;
            }
            robot.setCriticalDist(criticalDist);
            robot.setActiveDist(activeDist);
            robot.setRandom(roboInitRandom);
            robot.setX(X);
            robot.setY(Y);
            robot.setSpeed(1);
            robot.setSimulation(this);
            targets.forEach(robot::addTarget);
            if (i < countPhilistine)
                robot.setRobotType(AnyLogicRobot.Type.philistine);
            else if (i < countPhilistine + countGoodBoy) {
                robot.setRobotType(AnyLogicRobot.Type.goodboy);
                robot.setwF(confidenceGoodBoy);
            }
            else
                robot.setRobotType(AnyLogicRobot.Type.enemy);
            i++;
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
        if (!f) status = SimulationStatus.ENDED;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(positionSeed).append(" ");
        str.append(roboInitSeed).append(" ");
        str.append(activeDist).append(" ");
        str.append(criticalDist).append(" ");
        str.append(countPhilistine).append(" ");
        str.append(countGoodBoy).append(" ");
        str.append(countEnemy).append(" ");
        str.append(confidenceGoodBoy).append(" ");

        int leftTarget  = Integer.parseInt(targets.get(0).toString()) +
                          Integer.parseInt(targets.get(2).toString()) +
                          Integer.parseInt(targets.get(4).toString());
        int rightTarget = Integer.parseInt(targets.get(1).toString()) +
                          Integer.parseInt(targets.get(3).toString()) +
                          Integer.parseInt(targets.get(5).toString());
        str.append(leftTarget).append(" ");
        str.append(rightTarget).append(" ");
        return str.toString();
    }

    @Override
    public void setSimulationParam(HashMap<String, String> param) {
        super.setSimulationParam(param);

        if (param.get("allRandom") != null && Boolean.parseBoolean(param.get("allRandom"))) {
            Random random = new Random();
            activeDist = random.nextDouble() * (30 - 10) + 10;
            criticalDist = random.nextDouble() * (5 - 1) + 1;
            confidenceGoodBoy = random.nextDouble() * (1.0 - 0.1) + 0.1;

            int N = random.nextInt(901) + 100;
            int percentGoodBoys = random.nextInt(51) + 1;
            int percentEnemies = random.nextInt(34) + 1;

//            System.out.println(N + " " + percentGoodBoys + " " + percentEnemies);

            countGoodBoy = (int)((double)N / 100 * percentGoodBoys);
            countEnemy = (int)((double)N / 100 * percentEnemies);
            countPhilistine = N - countEnemy - countGoodBoy;
        }
        if (param.get("criticalDist") != null) criticalDist = Double.parseDouble(param.get("criticalDist"));
        if (param.get("activeDist") != null) activeDist = Double.parseDouble(param.get("activeDist"));
        if (param.get("positionSeed") != null) positionSeed = Integer.parseInt(param.get("positionSeed"));
        if (param.get("poboInitSeed") != null) roboInitSeed = Integer.parseInt(param.get("poboInitSeed"));
        if (param.get("countPhilistine") != null) countPhilistine = Integer.parseInt(param.get("countPhilistine"));
        if (param.get("countGoodBoy") != null) countGoodBoy = Integer.parseInt(param.get("countGoodBoy"));
        if (param.get("countEnemy") != null) countEnemy = Integer.parseInt(param.get("countEnemy"));
        if (param.get("distBetweenRobot") != null)
            distBetweenRobot = Double.parseDouble(param.get("distBetweenRobot"));
        if (param.get("confidenceGoodBoy") != null)
            confidenceGoodBoy = Double.parseDouble(param.get("confidenceGoodBoy"));
    }

    // только для инициализации. потом не будет
    private boolean checkRobotXY(int X, int Y) {
        for (Robot robot : robots)
            if (robot.getX() == X && robot.getY() == Y)
                return true;
        return false;
    }
}