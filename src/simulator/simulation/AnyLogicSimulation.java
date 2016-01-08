package simulator.simulation;

import org.w3c.dom.Node;
import simulator.configuration.Configuration;
import simulator.robot.AnyLogicRobot;
import simulator.robot.Robot;
import simulator.target.Target;

public class AnyLogicSimulation extends Simulation{

    @Override
    public void init() {

        double criticalDist = 4;
        if (initConf != null) {
            Node node = initConf.getChildNodes().item(1);
            criticalDist = Double.parseDouble(node.getAttributes().getNamedItem("value").getTextContent());
        }

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
                robot.setX(300 + i * (4 + Math.floor(criticalDist)));
                robot.setY(400 + j * (4 + Math.floor(criticalDist)));
                robot.setSpeed(2);
                robot.setSimulation(this);
                targets.forEach(robot::addTarget);
                robots.add(robot);
                if (robot instanceof AnyLogicRobot) {       // костыль, но пока так
                    ((AnyLogicRobot) robot).setRobotType(AnyLogicRobot.Type.philistine);
                    ((AnyLogicRobot) robot).setCriticalDist(criticalDist);
                } else {
                    System.out.println("Неприменимый тип. Симуляция не инициализированна до конца!");
                    System.out.println(robot.getClass().getName());
                    return;
                }
            }

        // куча костылей
        ((AnyLogicRobot)robots.get(10)).setRobotType(AnyLogicRobot.Type.enemy);
        ((AnyLogicRobot)robots.get(17)).setRobotType(AnyLogicRobot.Type.enemy);
        ((AnyLogicRobot)robots.get(33)).setRobotType(AnyLogicRobot.Type.enemy);
        ((AnyLogicRobot)robots.get(25)).setRobotType(AnyLogicRobot.Type.enemy);
        ((AnyLogicRobot)robots.get(13)).setRobotType(AnyLogicRobot.Type.enemy);
        ((AnyLogicRobot)robots.get(21)).setRobotType(AnyLogicRobot.Type.enemy);
        ((AnyLogicRobot)robots.get(100)).setRobotType(AnyLogicRobot.Type.enemy);
        ((AnyLogicRobot)robots.get(170)).setRobotType(AnyLogicRobot.Type.enemy);
        ((AnyLogicRobot)robots.get(290)).setRobotType(AnyLogicRobot.Type.enemy);
        ((AnyLogicRobot)robots.get(250)).setRobotType(AnyLogicRobot.Type.enemy);
        ((AnyLogicRobot)robots.get(130)).setRobotType(AnyLogicRobot.Type.enemy);
        ((AnyLogicRobot)robots.get(210)).setRobotType(AnyLogicRobot.Type.enemy);

        ((AnyLogicRobot)robots.get(23)).setRobotType(AnyLogicRobot.Type.goodboy);
        ((AnyLogicRobot)robots.get(11)).setRobotType(AnyLogicRobot.Type.goodboy);
        ((AnyLogicRobot)robots.get(37)).setRobotType(AnyLogicRobot.Type.goodboy);
        ((AnyLogicRobot)robots.get(52)).setRobotType(AnyLogicRobot.Type.goodboy);
        ((AnyLogicRobot)robots.get(135)).setRobotType(AnyLogicRobot.Type.goodboy);
        ((AnyLogicRobot)robots.get(217)).setRobotType(AnyLogicRobot.Type.goodboy);
        ((AnyLogicRobot)robots.get(111)).setRobotType(AnyLogicRobot.Type.goodboy);
        ((AnyLogicRobot)robots.get(170)).setRobotType(AnyLogicRobot.Type.goodboy);
        ((AnyLogicRobot)robots.get(267)).setRobotType(AnyLogicRobot.Type.goodboy);
        ((AnyLogicRobot)robots.get(243)).setRobotType(AnyLogicRobot.Type.goodboy);
        ((AnyLogicRobot)robots.get(142)).setRobotType(AnyLogicRobot.Type.goodboy);
        ((AnyLogicRobot)robots.get(277)).setRobotType(AnyLogicRobot.Type.goodboy);

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
