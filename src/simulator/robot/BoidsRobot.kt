package simulator.robot

import simulator.helper.Vector2D
import simulator.target.Target
import java.awt.Color
import java.awt.Graphics
import java.util.*

open class BoidsRobot : Robot() {

    enum class Type {NORMAl, INFORM, ENEMY}
    lateinit var type  : Type
    lateinit var color : Color

    var criticalDist = 0.toDouble()              // критическое расстояние
    var activeDist   = 0.toDouble()              // расстояние взаимодействия
    var w            = 0.toDouble()              // уверенность в цели
    protected var g    = Vector2D(0.0, 0.0)      // вектор на цель
    protected var h    = Vector2D(0.0, 0.0)      // вектор направление соседей
    protected var angle= Vector2D(0.0, -1.0)     // направление

    var mu: Double = 0.7
    var deltaW: Double = 0.05

    protected var target: Target? = null
    protected var neighbors = ArrayList<BoidsRobot>()

    override fun doStep() {
        if (!isActive || !simulation.field.contains(x, y) || simulation.anyTargetContains(x, y)) {
            isActive = false
            return
        }

        findNeighbors()
        calcH()
        calcG()
        calcW()
        calcTeta()

        var tmp = Vector2D(angle)
        var teta = tmp.angle()
        if (neighborsInSector(teta))
            if (!neighborsInSector(teta + Math.PI / 2))      tmp.rotate( Math.PI / 2)
            else if (!neighborsInSector(teta - Math.PI / 2)) tmp.rotate(-Math.PI / 2)
            else if (!neighborsInSector(teta + Math.PI))     tmp.rotate( Math.PI)
            else return

        x += tmp.x * speed
        y += tmp.y * speed
    }

    fun findNeighbors() {
        neighbors.clear();
        for (robot in simulation.robots) {
            if (robot == this || !robot.isActive) continue
            if (calcHypotenuse(x - robot.x, y - robot.y) < activeDist)
                neighbors.add(robot as BoidsRobot)
        }
    }

    open fun calcH() {
        if (neighbors.isEmpty()) return
        h = neighbors.fold(Vector2D(0.0, 0.0), { v, robot -> v + robot.angle})
        h.normalize()
    }

    open fun calcG() {
        if (target != null && type != Type.NORMAl) {
            g.set(target!!.centerX - x, target!!.centerY - y)
            g.normalize()
        }
    }

    open fun calcW() {

    }

    open fun calcTeta() {
        angle = g * w + h * (1 - w)
        angle.normalize()
    }

    override fun addTarget(newTarget: Target?) {
        target = newTarget
    }

    protected fun neighborsInSector(angle: Double): Boolean {
        val d = criticalDist
        val x1 = x + Math.cos(angle) * d
        val y1 = y + Math.sin(angle) * d

        if (Math.cos(angle) != 0.0 && Math.sin(angle) != 0.0) {
            val k  = Math.sin(angle) / Math.cos(angle)
            val k1 = -Math.cos(angle) / Math.sin(angle)
            val b  = y1 - k1 * x1
            val b1 = y - k1 * x
            val x2 = x - d / Math.sqrt(k1 * k1 + 1)
            val x3 = x + d / Math.sqrt(k1 * k1 + 1)
            val y2 = k1 * x2 + b1
            val y3 = k1 * x3 + b1
            val b2 = y2 - k * x2
            val b3 = y3 - k * x3

            for (a in neighbors) {
                var result1 = false
                var result2 = false
                if (b2 > b3 && a.y < k * a.x + b2 && a.y > k * a.x + b3) {
                    result1 = true
                } else if (b2 < b3 && a.y > k * a.x + b2 && a.y < k * a.x + b3) {
                    result1 = true
                }
                if (b > b1 && a.y > k1 * a.x + b1 && a.y < k1 * a.x + b) {
                    result2 = true
                } else if (b < b1 && a.y < k1 * a.x + b1 && a.y > k1 * a.x + b) {
                    result2 = true
                }
                if (result1 && result2) {
                    return true
                }
            }
            return false
            //        } else if (Math.sin(teta) == 0.0) {
        } else if (this.angle.y == 0.0) {
            val y2 = y - d
            val y3 = y + d
            for (a in neighbors) {
                var result1 = false
                var result2 = false
                if (y2 > y3 && a.y < y2 && a.y > y3) {
                    result1 = true
                } else if (y2 < y3 && a.y > y2 && a.y < y3) {
                    result1 = true
                }
                if (x > x1 && a.x > x1 && a.x < x) {
                    result2 = true
                } else if (x < x1 && a.x < x1 && a.x > x) {
                    result2 = true
                }
                if (result1 && result2) {
                    return true
                }
            }
            return false
            //        } else if (Math.cos(teta) == 0.0) {
        } else if (this.angle.x == 0.0) {
            val x2 = x - d
            val x3 = x + d

            for (a in neighbors) {
                var result1 = false
                var result2 = false
                if (x2 > x3 && a.x < x2 && a.x > x3) {
                    result1 = true
                } else if (x2 < x3 && a.x > x2 && a.x < x3) {
                    result1 = true
                }
                if (y > y1 && a.y > y1 && a.y < y) {
                    result2 = true
                } else if (y < y1 && a.y < y1 && a.y > y) {
                    result2 = true
                }
                if (result1 && result2) {
                    return true
                }
            }
            return false
        }
        return false
    }

    fun setRobotType(type: Type) {
        this.type = type
        when (type) {
            Type.NORMAl -> color = Color.BLUE
            Type.INFORM -> color = Color.GREEN
            Type.ENEMY -> color = Color.RED
        }
    }

    override fun draw(g: Graphics) {
        g.color = color
        g.fillOval(x.toInt() - 2, y.toInt() - 2, 4, 4)

        g.drawLine(x.toInt(), y.toInt(), (x + 10 * angle.x).toInt(), (y + 10 * angle.y).toInt());
    }

    protected fun calcHypotenuse(x: Double, y: Double): Double {
        return Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0))
    }
}
