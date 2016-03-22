package simulator.robot;

public class RobotConfidenceAlgA extends AnyLogicRobotConfidence{

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
}
