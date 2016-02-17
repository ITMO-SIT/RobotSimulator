package simulator.simulation.ready;

import simulator.services.Configuration;
import simulator.robot.AnyLogicRobot;
import simulator.robot.Robot;
import simulator.simulation.Simulation;
import simulator.simulation.SimulationStatus;
import simulator.target.Target;

import java.util.HashMap;
import java.util.Random;

public class AnyLogicSimulation extends Simulation<AnyLogicRobot> {

    private double criticalDist = 5;        // минимальное расстояние
    private double activeDist   = 20;       // расстоние

    private int countPhilistine = 300;   // количество обычных роботов
    private int countGoodBoy = 30;       // количество хороших роботов
    private int countEnemy = 5;          // количество плохих роботов

    private double confidenceGoodBoy = 0.1;

    private Integer positionSeed;
    private Integer roboInitSeed;

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

        int N = countPhilistine + countGoodBoy + countEnemy;

        int cols = 20;
        int rows  = (int) Math.ceil((double) N / cols);

        // TODO: Random с getSeed(). А то костыль ужвсный глаз мозолит
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

        for (int i = 0; i < N; i++) {
            int X, Y;
            while (true) {
                X = (int)(300 + positionRandom.nextInt(cols) * (4 + criticalDist));
                Y = (int)(400 + positionRandom.nextInt(rows) * (4 + criticalDist));
                if (!checkRobotXY(X, Y)) break;
            }
            AnyLogicRobot robot = (AnyLogicRobot) conf.newRobotInstance();
            robot.setCriticalDist(criticalDist);
            robot.setActiveDist(activeDist);
            robot.setRandom(roboInitRandom);
            robot.setX(X);
            robot.setY(Y);
            robot.setSpeed(1);
            robot.setSimulation(this);
            targets.forEach(robot::addTarget);
            robots.add(robot);
            if (i < countPhilistine)
                robot.setRobotType(AnyLogicRobot.Type.philistine);
            else if (i < countPhilistine + countGoodBoy) {
                robot.setRobotType(AnyLogicRobot.Type.goodboy);
                robot.setwF(confidenceGoodBoy);
            }
            else
                robot.setRobotType(AnyLogicRobot.Type.enemy);
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

        str.append(targets.get(0).toString()).append(" ");
        str.append(targets.get(1).toString()).append(" ");
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
        } else {
            if (param.get("criticalDist") != null) criticalDist = Double.parseDouble(param.get("criticalDist"));
            if (param.get("activeDist") != null) activeDist = Double.parseDouble(param.get("activeDist"));
            if (param.get("positionSeed") != null) positionSeed = Integer.parseInt(param.get("positionSeed"));
            if (param.get("poboInitSeed") != null) roboInitSeed = Integer.parseInt(param.get("poboInitSeed"));
            if (param.get("countPhilistine") != null) countPhilistine = Integer.parseInt(param.get("countPhilistine"));
            if (param.get("countGoodBoy") != null) countGoodBoy = Integer.parseInt(param.get("countGoodBoy"));
            if (param.get("countEnemy") != null) countEnemy = Integer.parseInt(param.get("countEnemy"));
            if (param.get("confidenceGoodBoy") != null)
                confidenceGoodBoy = Double.parseDouble(param.get("confidenceGoodBoy"));
        }
    }

    // только для инициализации. потом не будет
    private boolean checkRobotXY(int X, int Y) {
        for (Robot robot : robots)
            if (robot.getX() == X && robot.getY() == Y)
                return true;
        return false;
    }
}