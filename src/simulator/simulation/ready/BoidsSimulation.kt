package simulator.simulation.ready

import simulator.helper.params.SimulationParam
import simulator.robot.BoidsRobot
import simulator.simulation.Simulation
import simulator.simulation.SimulationStatus
import java.awt.Color
import java.util.*

class BoidsSimulation : Simulation<BoidsRobot>() {

    private var criticalDist = 5.0        // минимальное расстояние
    private var activeDist = 20.0       // расстоние взаимодействия

    private var countNormal = 300   // количество обычных роботов
    private var countInform = 30       // количество хороших роботов
    private var countEnemy = 5          // количество плохих роботов

    private var confidence = 0.1
    private var distBetweenRobot = criticalDist

    private var positionSeed: Long = 666
    private var deltaW = 0.05
    private var mu = 0.99


    override fun init() {
        robots.clear()
        targets.clear()

        var field  = createField()
        field.setSize(1000, 1500)

        var targetGood = createTarget()
        targetGood.setSize(100.toDouble(), 100.toDouble())
        targetGood.x = 0.0
        targetGood.y = 0.0
        targetGood.setColor(Color(50, 250, 50, 150))

        var targetBad = createTarget()
        targetBad.setSize(100.toDouble(), 100.toDouble())
        targetBad.x = field.width - targetBad.width
        targetBad.y = 0.0


        val number_robot = countNormal + countInform + countEnemy
        val robots = createRobot(number_robot)

        val cols = 20
        val rows = Math.ceil(number_robot.toDouble() / cols).toInt()

        val positionRandom = Random(positionSeed)

        var i = 0
        for (robot in robots) {
            var x: Double
            var y: Double
            while (true) {
                x = (300 + positionRandom.nextInt(cols) * distBetweenRobot)
                y = (400 + positionRandom.nextInt(rows) * distBetweenRobot)
                if (!robots.any { robot -> robot.x == x && robot.y == y }) break
            }

            robot.criticalDist = criticalDist
            robot.activeDist = activeDist
            robot.x = x
            robot.y = y
            robot.speed = 1.0
            robot.mu = mu
            robot.deltaW = deltaW
            robot.setSimulation(this)
            robot.isActive = true

            if (i < countNormal)
                robot.setRobotType(BoidsRobot.Type.NORMAl)
            else if (i < countNormal + countInform) {
                robot.setRobotType(BoidsRobot.Type.INFORM)
                robot.w = confidence
                robot.addTarget(targetGood)
            } else {
                robot.setRobotType(BoidsRobot.Type.ENEMY)
                robot.w = 1.toDouble()
                robot.addTarget(targetBad)
            }
            i++
        }

//        for (robot in robots) {
//            robot.findNeighbors()
//            robot.calcH()
//            robot.calcG()
//            robot.calcTeta()
//        }
        status = SimulationStatus.INITIALIZED
    }

    override fun nextIteration() {
        var f = false
        for (robot in robots) {
            if (robot.isActive) f = true
            robot.doStep()
        }
        if (!f) status = SimulationStatus.ENDED
    }

    override fun toString(): String {
        val str = StringBuilder()
        str.append(positionSeed).append(" ")
        str.append(activeDist).append(" ")
        str.append(criticalDist).append(" ")
        str.append(countNormal).append(" ")
        str.append(countInform).append(" ")
        str.append(countEnemy).append(" ")
        str.append(confidence).append(" ")
        str.append(distBetweenRobot).append(" ")
        str.append(mu).append(" ")
        str.append(deltaW).append(" ")

        val leftTarget = Integer.parseInt(targets[0].toString())/* +
                Integer.parseInt(targets[2].toString()) +
                Integer.parseInt(targets[4].toString())*/
        val rightTarget = Integer.parseInt(targets[1].toString())/* +
                Integer.parseInt(targets[3].toString()) +
                Integer.parseInt(targets[5].toString())*/
        str.append(leftTarget).append(" ")
        str.append(rightTarget).append(" ")
        return str.toString()
    }

    override fun setSimulationParam(param: HashMap<String, SimulationParam>) {
        super.setSimulationParam(param)

        positionSeed = param["positionSeed"]?.getValue<Long>() ?: Random().nextLong()

        criticalDist = param["criticalDist"]?.getValue<Double>() ?: 5.0
        activeDist   = param["activeDist"]?.getValue<Double>() ?: 20.0
        confidence   = param["confidenceGoodBoy"]?.getValue<Double>() ?: 0.5
        distBetweenRobot = param["distBetweenRobot"]?.getValue<Double>() ?: criticalDist
        if (distBetweenRobot < criticalDist) distBetweenRobot = criticalDist

        if (param["countAgent"] != null) {
            val N = param["countAgent"]!!.getValue<Int>()
            val percentG = param["percentGoodBoy"]?.getValue<Int>() ?: 10
            val percentE = param["percentEnemy"]?.getValue<Int>() ?: 5

            countInform = (N.toDouble() / 100 * percentG).toInt()
            countEnemy  = (N.toDouble() / 100 * percentE).toInt()
            countNormal = N - countInform - countEnemy
        } else {
            countNormal = param["countPhilistine"]?.getValue<Int>() ?: 265
            countInform = param["countGoodBoy"]?.getValue<Int>() ?: 30
            countEnemy  = param["countEnemy"]?.getValue<Int>() ?: 5
        }

        mu = param["mu"]?.getValue<Double>() ?: 0.99
        deltaW = param["deltaW"]?.getValue<Double>() ?: 0.05
    }

}