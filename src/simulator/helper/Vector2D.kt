@file:JvmName("VectorHelper")

package simulator.helper


// TODO: define hashCode()!!!
class Vector2D(var x: Double, var y: Double) {

    constructor(v: Vector2D): this(v.x, v.y)

    fun set(x: Double = 0.0, y: Double = 0.0) {
        this.x = x
        this.y = y
    }

    fun set(vector: Vector2D) = set(vector.x, vector.y)
    fun zero() = set()

    fun length() = Math.sqrt(x * x + y * y)

    fun normalize() {
        var len = length()
        if (len > 0.0) {
            x /= len
            y /= len
        }
    }

    // поворот против часовой стрелке на rot
    fun rotate(rot: Double) {
        var x = this.x * Math.cos(rot) - this.y * Math.sin(rot)
        var y = this.y * Math.cos(rot) + this.x * Math.sin(rot)
        this.x = x
        this.y = y
    }


    fun angle(): Double = if (y >= 0) angleBetween(this, Vector2D(1.0, 0.0))
                          else Math.PI * 2 - angleBetween(this, Vector2D(1.0, 0.0))

    operator fun plus(vector: Vector2D) = Vector2D(x + vector.x, y + vector.y)

    operator fun times(mul: Double) = Vector2D(mul * x, mul * y)

    operator fun div(other: Double) = Vector2D(x / other, y / other)
    operator fun div(other: Int)    = Vector2D(x / other, y / other)

    override fun equals(other: Any?): Boolean = other is Vector2D && x == other.x && y == other.y
}

fun scalarProduct(v1: Vector2D, v2: Vector2D): Double = v1.x * v2.x + v1.y * v2.y

fun angleBetween(v1: Vector2D, v2: Vector2D): Double = Math.acos(scalarProduct(v1, v2) / (v1.length() * v2.length()))
fun   cosBetween(v1: Vector2D, v2: Vector2D): Double = scalarProduct(v1, v2) / (v1.length() * v2.length())