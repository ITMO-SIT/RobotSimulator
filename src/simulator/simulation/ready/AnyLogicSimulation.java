package simulator.simulation.ready;

import simulator.helper.InputSimulationParam;
import simulator.robot.AnyLogicRobot;
import simulator.robot.Robot;
import simulator.simulation.Simulation;
import simulator.simulation.SimulationStatus;
import simulator.target.Target;

import java.util.*;

public class AnyLogicSimulation extends Simulation<AnyLogicRobot> {

    @InputSimulationParam private double criticalDist = 5;        // минимальное расстояние
    @InputSimulationParam private double activeDist   = 20;       // расстоние

    @InputSimulationParam private int countPhilistine = 300;   // количество обычных роботов
    @InputSimulationParam private int countGoodBoy = 30;       // количество хороших роботов
    @InputSimulationParam private int countEnemy = 5;          // количество плохих роботов

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
                X = (int)(300 + positionRandom.nextInt(cols) * (4 + criticalDist));
                Y = (int)(400 + positionRandom.nextInt(rows) * (4 + criticalDist));
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
            else if (i < countPhilistine + countGoodBoy)
                robot.setRobotType(AnyLogicRobot.Type.goodboy);
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

        str.append(targets.get(0).toString()).append(" ");
        str.append(targets.get(1).toString()).append(" ");
        return str.toString();
    }

    @Override
    public void setSimulationParam(HashMap<String, String> param) {
        super.setSimulationParam(param);

        if (param.get("criticalDist") != null) criticalDist = Double.parseDouble(param.get("criticalDist"));
        if (param.get("activeDist") != null) activeDist = Double.parseDouble(param.get("activeDist"));
        if (param.get("positionSeed") != null) positionSeed = Integer.parseInt(param.get("positionSeed"));
        if (param.get("poboInitSeed") != null) roboInitSeed = Integer.parseInt(param.get("poboInitSeed"));
        if (param.get("countPhilistine") != null) countPhilistine = Integer.parseInt(param.get("countPhilistine"));
        if (param.get("countGoodBoy") != null) countGoodBoy = Integer.parseInt(param.get("countGoodBoy"));
        if (param.get("countEnemy") != null) countEnemy = Integer.parseInt(param.get("countEnemy"));
    }

    // только для инициализации. потом не будет
    private boolean checkRobotXY(int X, int Y) {
        for (Robot robot : robots)
            if (robot.getX() == X && robot.getY() == Y)
                return true;
        return false;
    }
}