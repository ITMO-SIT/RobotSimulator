package simulator.robot

import simulator.helper.cosBetween
import java.awt.Color

class BoidsRobotConfAlgB : BoidsRobot() {

    override fun calcW() {
        if (type != Type.INFORM) return

        if (cosBetween(h, g) >= mu) w = Math.min(w + deltaW, 1.0)
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
