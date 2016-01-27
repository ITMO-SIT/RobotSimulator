package simulator.robot;

import simulator.target.Target;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

public class AnyLogicRobot extends Robot {

    private static final Random RANDOM = new Random(666);

    public enum Type {philistine, goodboy, enemy}

    protected Type robotType;
    protected Color color;

    protected ArrayList<AnyLogicRobot> neighbors;
    protected double criticalDist/*= 4*/;
    protected double activeDist/* = 20*/;

    protected double  teta;
    protected double  wT, wF;
    protected Point2D gT, gF;
    protected Point2D h;


    public AnyLogicRobot() {
        isActive = true;
        neighbors = new ArrayList<>();
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int)x - 2, (int)y - 2, 4, 4);
//        if (wF == 1 || wT != 0)
//            g.drawOval((int)(x - activeDist), (int)(y - activeDist),
//                       (int)activeDist * 2, (int) activeDist * 2);
    }

    public void setRobotType(Type type) {
        robotType = type;
        switch (robotType) {
            case philistine:
                wT = 0;
                wF = 0;
                color = Color.BLUE;
                break;
            case goodboy:
                wT = 0.1;
                wF = 0;
                color = Color.GREEN;
                break;
            case enemy:
                wT = 0;
                wF = 1;
                color = Color.RED;
                break;
        }
        gT = new Point2D.Double(targets.get(0).getCenterX(), targets.get(0).getCenterY());
        gF = new Point2D.Double(targets.get(1).getCenterX(), targets.get(1).getCenterY());

        h = new Point2D.Double(RANDOM.nextDouble() - 0.5, 0.2 - RANDOM.nextDouble());
        findNeighbors();
        calcH();

        if (wT == 0)
            teta = RANDOM.nextDouble() * Math.PI * 2;
        else
            calcTeta();
    }

    public void setCriticalDist(double dist) {criticalDist = dist;}
    public void setActiveDist(double dist)   {activeDist = dist;}

    @Override
    public void doStep() {
        if (!isActive) return;
        targets.stream().filter(target -> target.contains(x, y)).forEach(target -> targetDone());
        if (x < 0 || y < 0 || x > 1000 || y > 800) {
            isActive = false;
            return;
        }

        findNeighbors();
        calcH();
        calcCorrectDist();
        calcG();
        calcTeta();
        teta = teta !=0 ?  teta : RANDOM.nextDouble()* Math.PI / 2; // Зачем?!

        double v = speed;
        if (neighborsInSector(teta))
            if (!neighborsInSector(teta + Math.PI / 2)) {
                teta = teta + Math.PI / 2;
            }
            else if (!neighborsInSector(teta - Math.PI / 2)) {
                teta = teta - Math.PI / 2;
            }
            else if (!neighborsInSector(teta + Math.PI) ) {
                teta = teta + Math.PI;
            }
            else {
                v = 0;
            }

        x += Math.cos(teta) * v;
        y += Math.sin(teta) * v;
    }

    @Override
    public void targetDone() {
        isActive = false;
    }

    // скорее всего в будущем реализация данного метода отойдет к симуляции
    private void findNeighbors() {
        neighbors.clear();
        for (AnyLogicRobot robot : (ArrayList<AnyLogicRobot>)simulation.getRobots()) {
            if (robot == this) continue;
            if (!robot.isActive()) continue;
            if (calcHypotenuse(x - robot.getX(), y - robot.getY()) < activeDist) {
                neighbors.add(robot);
            }
        }
    }

    private boolean neighborsInSector(double angle) {
        double d = criticalDist;
        double x1 = x + Math.cos(angle) * d;
        double y1 = y + Math.sin(angle) * d;

        if (Math.cos(angle) != 0 && Math.sin(angle) != 0) {
            double k = Math.sin(angle) / Math.cos(angle);
            double k1 = - Math.cos(angle) / Math.sin(angle);
            double b = y1 - k1 * x1;
            double b1 = y - k1 * x;
            double x2 = x - d / Math.sqrt(k1 * k1 + 1);
            double x3 = x + d / Math.sqrt(k1 * k1 + 1);
            double y2 = k1 * x2 + b1;
            double y3 = k1 * x3 + b1;
            double b2 = y2 - k * x2;
            double b3 = y3 - k * x3;

            for (Robot a: neighbors) {
                boolean result1 = false, result2 = false;
                if ((b2 > b3) && (a.y < k * a.x + b2) && (a.y > k * a.x + b3)) {
                    result1 = true;
                }
                else if ((b2 < b3) && (a.y > k * a.x + b2) && (a.y < k * a.x + b3)) {
                    result1 = true;
                }
                if ((b > b1) && (a.y > k1 * a.x + b1) && (a.y < k1 * a.x + b)) {
                    result2 = true;
                }
                else if ((b < b1) && (a.y < k1 * a.x + b1) && (a.y > k1 * a.x + b)) {
                    result2 = true;
                }
                if (result1 && result2) {
                    return true;
                }
            }
            return false;
        }
        else if (Math.sin(teta) == 0) {
            double y2 = y - d;
            double y3 = y + d;
            for (Robot a: neighbors) {
                boolean result1 = false, result2 = false;
                if ((y2 > y3) && (a.y < y2) && (a.y > y3)) {
                    result1 = true;
                }
                else if ((y2 < y3) && (a.y > y2) && (a.y < y3)) {
                    result1 = true;
                }
                if ((x > x1) && (a.x > x1) && (a.x < x)) {
                    result2 = true;
                }
                else if ((x < x1) && (a.x < x1) && (a.x > x)) {
                    result2 = true;
                }
                if (result1 && result2) {
                    return true;
                }
            }
            return false;
        }
        else if (Math.cos(teta) == 0) {
            double x2 = x - d;
            double x3 = x + d;

            for (Robot a: neighbors) {
                boolean result1 = false, result2 = false;
                if ((x2 > x3) && (a.x < x2) && (a.x > x3)) {
                    result1 = true;
                }
                else if ((x2 < x3) && (a.x > x2) && (a.x < x3)) {
                    result1 = true;
                }
                if ((y > y1) && (a.y > y1) && (a.y < y)) {
                    result2 = true;
                }
                else if ((y < y1) && (a.y < y1) && (a.y > y)) {
                    result2 = true;
                }
                if (result1 && result2) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private void calcTeta() {
        double tempX = wT * gT.getX() + wF * gF.getX() + (1 - wT - wF)*h.getX();
        double tempY = wT * gT.getY() + wF * gF.getY() + (1 - wT - wF)*h.getY();
        if (tempX != 0 ) {
            teta = Math.atan(tempY / tempX);
            if (tempX <= 0 && tempY <= 0)
                teta += Math.PI;
            else if (tempX >= 0 && tempY >= 0)
                teta = -teta;
            else if (tempX <= 0 && tempY >= 0)
                teta = Math.PI - teta;
        } else
            teta = 0;
    }

    private void calcG() {
        Target target = targets.get(0);
        double tempX = target.getCenterX() - x;
        double tempY = target.getCenterY() - y;
        double dist = calcHypotenuse(tempX, tempY);
        gT.setLocation(tempX / dist, tempY / dist);

        target = targets.get(1);
        tempX = target.getCenterX() - x;
        tempY = target.getCenterY() - y;
        dist = calcHypotenuse(tempX, tempY);
        gF.setLocation(tempX / dist, tempY / dist);
    }

    private void calcH() {
        double sumCos = 0;
        double sumSin = 0;
        if (neighbors.isEmpty()) return;
        for (AnyLogicRobot robot : neighbors) {
            sumCos += Math.cos(robot.teta);
            sumSin += Math.sin(robot.teta);
        }
        h.setLocation(sumCos / neighbors.size(), sumSin / neighbors.size());
    }

    private void calcCorrectDist() {
        double corrDistX = 0;
        double corrDistY = 0;
        int count = 1;      // очень странное место
        for (AnyLogicRobot robot : neighbors) {
            double distX = robot.getX() - x;
            double distY = robot.getY() - y;
            double dist = calcHypotenuse(distX, distY);
            if (dist > 2 * criticalDist) {
                distX /= dist;
                distY /= dist;
                corrDistX += distX;
                corrDistY += distY;
                count++;
            }
        }
        corrDistX /= count;
        corrDistY /= count;
        h.setLocation(h.getX() + corrDistX / 10, h.getY() + corrDistY / 10);
    }

    private double calcHypotenuse(double x,double y) {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }
}
