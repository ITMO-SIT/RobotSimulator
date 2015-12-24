package simulator.configuration;

import simulator.field.DefaultField;
import simulator.field.Field;
import simulator.robot.DefaultRobot;
import simulator.robot.Robot;
import simulator.simulation.DefaultSimulation;
import simulator.simulation.Simulation;
import simulator.target.DefaultTarget;
import simulator.target.Target;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class Configuration {

    private static final Configuration instance = new Configuration();

    private Constructor constructorSimulation;
    private Constructor constructorRobot;
    private Constructor constructorTarget;
    private Constructor constructorField;

    private ArrayList<Robot>  robots;
    private ArrayList<Target> targets;
    private Simulation  simulation;
    private Field       field;

    static public Configuration getInstance() {
            return instance;
    }

    private Configuration() {
        robots   = new ArrayList<>();
        targets  = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("configuration.txt")));
            constructorSimulation = Class.forName(reader.readLine()).getConstructor();
            constructorRobot  = Class.forName(reader.readLine()).getConstructor();
            constructorTarget = Class.forName(reader.readLine()).getConstructor();
            constructorField  = Class.forName(reader.readLine()).getConstructor();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Simulation newSimulationInstance() {
        try {
            return simulation = (Simulation) constructorSimulation.newInstance();
        } catch (Exception e) {
            System.err.println("Ошибка создания симуляции");
            e.printStackTrace();
            return simulation = new DefaultSimulation();
        }
    }

    public Robot newRobotInstance() {
        Robot robot;
        try {
            robot = (Robot) constructorRobot.newInstance();
        } catch (Exception e) {
            System.err.println("Ошибка создания робота");
            e.printStackTrace();
            robot = new DefaultRobot();
        }
        robots.add(robot);
        return robot;
    }

    public Target newTargetInstance() {
        Target target;
        try {
            target = (Target) constructorTarget.newInstance();
        } catch (Exception e) {
            System.err.println("Ошибка создания цели");
            e.printStackTrace();
            target = new DefaultTarget();
        }
        targets.add(target);
        return target;
    }

    public Field newFieldInstance() {
        try {
            return field = (Field) constructorField.newInstance();
        } catch (Exception e) {
            System.err.println("Ошибка создания поля");
            e.printStackTrace();
            return field = new DefaultField();
        }
    }


    public void resetRobots()  {robots.clear();}
    public void resetTargets() {targets.clear();}

    // ------- getter region start -------//
    public Field      getField()      {return field;}
    public Simulation getSimulation() {return simulation;}
    public ArrayList<Target> getTargets() {return targets;}
    public ArrayList<Robot>  getRobots()  {return robots;}
    // ------- getter region end   -------//
}
