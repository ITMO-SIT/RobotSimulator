package simulator.robot;


import simulator.simulation.ready.ReputationSimulation;

import java.util.ArrayList;
import java.util.Random;

public class ReputationRobot extends Robot{

    int id;
    boolean goodBoy = true;
    double activeDist;
    ArrayList<ReputationRobot> neighbors = new ArrayList<>();

    public void setActiveDist(double activeDist) {
        this.activeDist = activeDist;
    }

    public void setGoodBoy(boolean goodBoy) {
        this.goodBoy = goodBoy;
    }

    public boolean isGoodBoy() {
        return goodBoy;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private double calcHypotenuse(double x, double y) {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public double[] calcTargetsDistance() {
        double [] result = new double[targets.size()];
        if (goodBoy) {
            for (int i = 0; i < targets.size(); i++) {
                result[i] = calcHypotenuse(x - targets.get(i).getX(), y - targets.get(i).getY());
            }
        }
        else {
            Random random = new Random();
            for (int i = 0; i < targets.size(); i++) {
                result[i] = random.nextDouble()*10;
            }
        }
        return result;
    }

    public void findNeighbors() {
        neighbors.clear();
        for (Object object : simulation.getRobots()) {
            ReputationRobot robot = (ReputationRobot) object;
            if (robot == this) continue;
            if (calcHypotenuse(x - robot.getX(), y - robot.getY()) < activeDist) {
                neighbors.add(robot);
            }
        }
    }

    public void checkNeighbors() {
        ((ReputationSimulation) simulation).V[id][id] = 1;
        if (goodBoy) {
            for (int i = 0; i < neighbors.size(); i++) {
                for (int j = 0; j < targets.size(); j++) {
                    double temp = calcHypotenuse(targets.get(j).getX() - neighbors.get(i).getX(), targets.get(j).getY() - neighbors.get(i).getY());
                    if (temp == ((ReputationSimulation) simulation).D[neighbors.get(i).getId()][j]) {
                        ((ReputationSimulation) simulation).V[id][neighbors.get(i).getId()] = 1;
                    } else {
                        ((ReputationSimulation) simulation).V[id][neighbors.get(i).getId()] = -1;
                        break;
                    }
                }
            }
        }
        else {
            ArrayList<ReputationRobot> repRob = ((ReputationSimulation) simulation).getRobots();
            for (int i = 0; i < repRob.size(); i++) {
                if (repRob.get(i).isGoodBoy()) {
                    if (neighbors.contains(repRob.get(i))) {
                        ((ReputationSimulation) simulation).V[id][repRob.get(i).getId()] = -1;
                    }
                }
                else {
                    ((ReputationSimulation) simulation).V[id][repRob.get(i).getId()] = 1;
                }
            }
        }
    }

    @Override
    public void doStep() {

    }

    @Override
    public void targetDone() {

    }
}
