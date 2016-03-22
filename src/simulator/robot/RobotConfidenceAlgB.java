package simulator.robot;

public class RobotConfidenceAlgB extends AnyLogicRobotConfidence {

    @Override
    protected void calcW() {
        if (!neighbors.isEmpty() && robotType != Type.enemy) {
            double normaG1 = calcHypotenuse(gT.getX(), gT.getY());
            double normaG2 = calcHypotenuse(gF.getX(), gF.getY());
            double normaH = calcHypotenuse(h.getX(), h.getY());
            double cosG1H = (h.getX() * gT.getX() + h.getY() * gT.getY()) / normaH / normaG1;
            double cosG2H = (h.getX() * gF.getX() + h.getY() * gF.getY()) / normaH / normaG2;

            if (cosG1H >= mu) {
                wT += deltaW;
                wF -= deltaW;
                if (wT > 1) wT = 1;
                if (wF < 0) wF = 0;
            } else if (cosG2H >= mu) {
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
