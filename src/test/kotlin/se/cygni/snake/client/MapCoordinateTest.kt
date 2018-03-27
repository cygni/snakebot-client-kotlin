package se.cygni.snake.client

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MapCoordinateTest {

    @Test
    @Throws(Exception::class)
    fun testTranslateBy() {
        val start = MapCoordinate(2, 1)
        val end = start.translateBy(3, 3)

        assertEquals(MapCoordinate(5, 4), end)
    }

    @Test
    @Throws(Exception::class)
    fun testGetManhattanDistanceTo() {
        val start = MapCoordinate(2, 1)
        val end = MapCoordinate(6, 7)
        assertEquals(10, start.getManhattanDistanceTo(end))
    }

    @Test
    @Throws(Exception::class)
    fun testGetManhattanDistanceIsZero() {
        val start = MapCoordinate(2, 1)
        assertEquals(0, start.getManhattanDistanceTo(start))
    }
}