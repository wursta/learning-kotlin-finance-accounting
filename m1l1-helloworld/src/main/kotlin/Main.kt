import kotlin.math.pow

fun calcCircleArea(radius: Float): Float {
    return (3.14 * radius.pow(2)).toFloat()
}
fun main() {
    print("Введите радиус в метрах: ")
    val radius: String? = readLine()

    if (radius === null || radius.isEmpty()) {
        println("Вы не ввели радиус")
        return
    }

    println("Радиус круга: " + calcCircleArea(radius.toFloat()) + " кв.м.")
}