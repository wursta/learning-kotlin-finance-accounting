import kotlin.test.Test
import kotlin.test.assertEquals

class StubTest {
    @Test
    fun testCalcCircleArea() {
        assertEquals(314.toFloat(), calcCircleArea(10.toFloat()))
        assertEquals(706.5.toFloat(), calcCircleArea(15.toFloat()))
        assertEquals(1256.toFloat(), calcCircleArea(20.toFloat()))
    }
}