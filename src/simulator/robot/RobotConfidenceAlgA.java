package simulator.robot;

public class RobotConfidenceAlgA extends AnyLogicRobotConfidence{

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
}
