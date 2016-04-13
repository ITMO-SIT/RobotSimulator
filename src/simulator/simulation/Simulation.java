package simulator.simulation;

import simulator.helper.Observable;
import simulator.helper.Observer;
import simulator.field.Field;
import simulator.helper.SimulatorEvent;
import simulator.helper.params.SimulationParam;
import simulator.robot.Robot;
import simulator.services.ClassStorage;
import simulator.target.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

abstract public class Simulation<R extends Robot> implements Runnable, Observable {
// ---------------------- поля класса ---------------------- //
    protected int delay;
    protected SimulationStatus status;

    // TODO: инкапсулировать все поля, что ниже
    protected final ArrayList<R>      robots;
    protected final ArrayList<Target> targets;
    protected Field field;

    private final ConcurrentLinkedQueue<Observer> observers;
    private String pathRobotClass;
    private String pathTargetClass;
    private String pathFieldClass;
// ---------------------- поля класса ---------------------- //
// ------------------- абстрактные методы ------------------ //
    abstract public void init();
    abstract public void nextIteration();
// ------------------- абстрактные методы ------------------ //

    protected Simulation() {
        robots    = new ArrayList<>();
        targets   = new ArrayList<>();
        observers = new ConcurrentLinkedQueue<>();
        status = SimulationStatus.NOT_INITIALIZED;
        delay  = 0;
    }

    public void setSimulationParam(HashMap<String, SimulationParam> param) {
        if (param.get("robot") != null) pathRobotClass  = param.get("robot") .getValue();
        if (param.get("target")!= null) pathTargetClass = param.get("target").getValue();
        if (param.get("field") != null) pathFieldClass  = param.get("field") .getValue();


//        if (param.get("name") != null) name = param.get("name");
//        java.lang.reflect.Field[] fields = this.getClass().getDeclaredFields();
//        for (java.lang.reflect.Field field : fields) {
//            if (field.isAnnotationPresent(InputSimulationParam.class))
//                System.out.println(field.getName());
//        }

        /*try {

            if (this.getClass().getSuperclass() != Simulation.class) {
//            setSimulationParam(param);
                System.out.println(this.getClass().getSuperclass().getMethod("setSimulationParam", HashMap.class).getName());
            }

            List<java.lang.reflect.Field> fields =
                ClassStorage.getInstance().getFieldWithAnnotation(this.getClass().getName(), InputSimulationParam.class);
            for (java.lang.reflect.Field field : fields) {
                String name  = field.getName();
                String value = param.get(name);
                System.out.println(name + " = " + value);
                if (value != null) {
                    Class classType = field.getType();
                    System.out.println(classType.toString());
                    if (classType.equals(Integer.class)) {
//                        field.setInt(this, Integer.parseInt(value));
//                        System.out.println(field.getName() + " = " + value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }

    public void start() {status = SimulationStatus.RUNNING;}
    public void pause() {status = SimulationStatus.PAUSE;}
    public void end()   {status = SimulationStatus.ENDED;}

    public final ArrayList<R>  getRobots()  {return robots;}
    public final ArrayList<Target> getTargets() {return targets;}
    public final SimulationStatus  getStatus()  {return status;}
    public final Field getField() {return field;}

    public void setDelay(int delay) {this.delay = delay;}


    public boolean anyTargetContains(double x, double y) {
        return targets.stream().anyMatch(target -> target.contains(x, y));
    }

    // TODO: обработку исключений
    protected final R createRobot() throws Exception {
        R robot = ClassStorage.getInstance().<R>newClassInstance(pathRobotClass);
        robots.add(robot);
        return robot;
    }

    protected final List<R> createRobot(int count) throws Exception {
        List<R> robots = ClassStorage.getInstance().<R>newClassInstance(pathRobotClass, count);
        this.robots.addAll(robots);
        return robots;
    }

    protected final Target createTarget() throws Exception {
        Target target = ClassStorage.getInstance().<Target>newClassInstance(pathTargetClass);
        targets.add(target);
        return target;
    }

    protected final Field createField() throws Exception {
        field = ClassStorage.getInstance().<Field>newClassInstance(pathFieldClass);
        return field;
    }

    // ------------- реализация интерфейса Observable ---------- //
    @Override
    public final void addObserver(Observer observer) {
            observers.add(observer);
    }

    @Override
    public final void removeObserver(Observer observer) {
            observers.remove(observer);
    }

    @Override
    public final void notifyObservers(SimulatorEvent event) {
        for (Observer observer : observers)
            observer.update(this, event);
    }
// ------------- реализация интерфейса Observable ---------- //
// -------------- реализация интерфейса Runnable ----------- //
    @Override
    public void run() {
        System.out.println("Запуск потока");
        status = SimulationStatus.RUNNING;
        while (status == SimulationStatus.RUNNING) {
            nextIteration();

            if (delay != 0)
                try {
                    Thread.sleep(delay);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            notifyObservers(SimulatorEvent.SIMULATION_STEP_END);
        }
        System.out.println("Завершение потока");
        switch (status) {
            case PAUSE:
                notifyObservers(SimulatorEvent.SIMULATION_PAUSE);
                break;
            case ENDED:
                notifyObservers(SimulatorEvent.SIMULATION_END);
                break;
        }
    }
// -------------- реализация интерфейса Runnable ----------- //
}
