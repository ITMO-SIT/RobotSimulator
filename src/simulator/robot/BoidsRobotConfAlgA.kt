package simulator.robot

import simulator.helper.Vector2D
import java.awt.Color

class BoidsRobotConfAlgA : BoidsRobot() {

    override fun calcW() {
        if (neighbors.isEmpty() || type != Type.INFORM) return

        var h = neighbors.fold(Vector2D(0.0, 0.0), { v, robot -> v + robot.angle }) / neighbors.size

        if (h.length() >= mu) w = Math.min(w + deltaW, 1.0)
        else w = Math.max(w - deltaW, 0.0)

        when {
            w >= 0.8 -> color = Color(29, 211, 0)
            w >= 0.6 -> color = Color(24, 138, 0)
            w >= 0.4 -> color = Color(0, 108, 81)
            w >= 0.2 -> color = Color(0, 166, 124)
            else -> color = Color.BLUE
        }
    }
}