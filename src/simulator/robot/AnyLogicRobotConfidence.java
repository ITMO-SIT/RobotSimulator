package simulator.robot;

public class AnyLogicRobotConfidence extends AnyLogicRobot {

    private double m = 0.99;
    private double deltaW = 0.05;

    private void calcW() {
        if (!neighbors.isEmpty() && robotType != Type.enemy) {
            double normaG1 = calcHypotenuse(gT.getX(), gT.getY());
            double normaG2 = calcHypotenuse(gF.getX(), gF.getY());
            double normaH = calcHypotenuse(h.getX(), h.getY());
            double cosG1H = (h.getX() * gT.getX() + h.getY() * gT.getY()) / normaH / normaG1;
            double cosG2H = (h.getX() * gF.getX() + h.getY() * gF.getY()) / normaH / normaG2;

            if (cosG1H >= m) {
                wT += deltaW;
                wF -= deltaW;
                if (wT > 1) wT = 1;
                if (wF < 0) wF = 0;
            } else if (cosG2H >= m) {
                wT -= deltaW;
                wF += deltaW;
                if (wT < 0) wT = 0;
                if (wF > 1) wF = 1;
            } else {
                wT -= deltaW;
                wF -= deltaW;
                if (wT < 0) wT = 0;
                if (wF < 0) wF = 0;
            }
        }
    }

    @Override
    public void doStep() {
        if (!isActive) return;
        targets.stream().filter(target -> target.contains(x, y)).forEach(target -> targetDone());
        if (x < 0 || y < 0 || x > 1000 || y > 2500) {
            isActive = false;
            return;
        }

        findNeighbors();
        calcH();
        calcCorrectDist();
        calcG();
        calcW();
        calcTeta();

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

    public void setM(double m) {this.m = m;}
    public void setDeltaW(double deltaW) {this.deltaW = deltaW;}
}
