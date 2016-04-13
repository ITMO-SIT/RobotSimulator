package simulator.robot;

import simulator.target.Target;

public class DefaultRobot extends Robot {

    public DefaultRobot() {
        isActive = true;
    }

    @Override
    public void doStep() {
        if (!isActive) return;
        targets.stream().filter(target -> target.contains(x, y)).forEach(target -> targetDone());

        double dist = Double.MAX_VALUE;
        double angle = 0f;
        for (Target target : targets) {
            double tempX = x - target.getCenterX();
            double tempY = y - target.getCenterY();
            double temp = Math.sqrt(Math.pow(tempX, 2) + Math.pow(tempY, 2));
            if (temp < dist) {
                dist = temp;

                angle = Math.atan(tempY / tempX);
                if (tempX < 0) angle -= Math.PI;
            }
        }

        x -= Math.cos(angle) * speed;
        y -= Math.sin(angle) * speed;
    }
}
