package simulator.robot;

abstract public class AnyLogicRobotConfidence extends AnyLogicRobot {

    protected double mu = 0.99;
    protected double deltaW = 0.05;

    protected int countIter = 0;

    protected abstract void calcW();

    @Override
    public void doStep() {
        if (!isActive || !simulation.getField().contains(x, y)) {
            isActive = false;
            return;
        }
        targets.stream().filter(target -> target.contains(x, y)).forEach(target -> targetDone());

        findNeighbors();
        calcH();
        calcCorrectDist();
        calcG();
        if (countIter > 15)
            calcW();
//        calcTeta();

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

        countIter++;
    }

    public void setM(double m) {this.mu = m;}
    public void setDeltaW(double deltaW) {this.deltaW = deltaW;}
}
