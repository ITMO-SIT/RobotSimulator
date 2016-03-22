package simulator.robot;

public class RobotConfidenceAlgB extends AnyLogicRobotConfidence {

    @Override
    protected void calcW() {
        if (neighbors.isEmpty() || robotType == Type.enemy) return;
        double corrDistX = 0;
        double corrDistY = 0;
        int count = 1;
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

        double norm = calcHypotenuse(corrDistX, corrDistY);

        if (norm >= mu) {
            wT += deltaW;
            if (wT > 1) wT = 1;
        } else {
            wT -= deltaW;
            if (wT < 0) wT = 0;
        }
    }

    @Override
    public void doStep() {
        if (!isActive || !simulation.getField().contains(x, y)) {
            isActive = false;
            return;
        }
        targets.stream().filter(target -> target.contains(x, y)).forEach(target -> targetDone());

        findNeighbors();
        calcH();
        calcG();
        calcW();

        double tempX = wT * gT.getX() + wF * gF.getX() + (1 - wT - wF)*h.getX();
        double tempY = wT * gT.getY() + wF * gF.getY() + (1 - wT - wF)*h.getY();
        teta = Math.atan(tempY / tempX);
        if (tempX < 0) teta -= Math.PI;

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
}
