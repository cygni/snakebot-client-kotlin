package se.cygni.snake.client

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import se.cygni.snake.client.api.model.*
import se.cygni.snake.client.api.model.Map

class MapUtilTest {
    /*
        0 1 2
        3 4 5
        6 7 8
    */
    @Test
    @Throws(Exception::class)
    fun testCanIMoveInDirectionNearEdge() {
        val snakeInfos = arrayOf<SnakeInfo>(SnakeInfo("a", 3, "a", intArrayOf(5, 8, 7), 0))
        val map = createMap(snakeInfos, IntArray(6), IntArray(3))

        val mapUtil = MapUtil(map, "a")

        assertTrue(mapUtil.canIMoveInDirection(SnakeDirection.UP))
        assertTrue(mapUtil.canIMoveInDirection(SnakeDirection.LEFT))
        assertFalse(mapUtil.canIMoveInDirection(SnakeDirection.DOWN))
        assertFalse(mapUtil.canIMoveInDirection(SnakeDirection.RIGHT))
    }

    @Test
    @Throws(Exception::class)
    fun testCanIMoveInDirection() {
        val snakeInfos = arrayOf<SnakeInfo>(SnakeInfo("a", 3, "a", intArrayOf(7, 4, 5, 8), 0))
        val map = createMap(snakeInfos, IntArray(6), IntArray(3))

        val mapUtil = MapUtil(map, "a")

        assertFalse(mapUtil.canIMoveInDirection(SnakeDirection.UP))
        assertTrue(mapUtil.canIMoveInDirection(SnakeDirection.LEFT))
        assertFalse(mapUtil.canIMoveInDirection(SnakeDirection.DOWN))
        assertFalse(mapUtil.canIMoveInDirection(SnakeDirection.RIGHT))
    }

    @Test
    @Throws(Exception::class)
    fun testIsTileAvailableForMovementTo() {
        val snakeInfos = arrayOf<SnakeInfo>(SnakeInfo("a", 3, "a", intArrayOf(7, 4, 5, 8), 0))
        val map = createMap(snakeInfos, IntArray(0), intArrayOf(0))

        val mapUtil = MapUtil(map, "a")

        assertFalse(mapUtil.isTileAvailableForMovementTo(MapCoordinate(1, 2)))
        assertFalse(mapUtil.isTileAvailableForMovementTo(MapCoordinate(1, 1)))
        assertFalse(mapUtil.isTileAvailableForMovementTo(MapCoordinate(2, 1)))
        assertFalse(mapUtil.isTileAvailableForMovementTo(MapCoordinate(2, 2)))
        assertFalse(mapUtil.isTileAvailableForMovementTo(MapCoordinate(-1, -1)))

        // Obstacle at 0,0
        assertFalse(mapUtil.isTileAvailableForMovementTo(MapCoordinate(0, 0)))

        assertTrue(mapUtil.isTileAvailableForMovementTo(MapCoordinate(0, 1)))
        assertTrue(mapUtil.isTileAvailableForMovementTo(MapCoordinate(0, 2)))
        assertTrue(mapUtil.isTileAvailableForMovementTo(MapCoordinate(1, 0)))
        assertTrue(mapUtil.isTileAvailableForMovementTo(MapCoordinate(2, 0)))
    }

    @Test
    @Throws(Exception::class)
    fun testGetSnakeSpread() {

        val snakeInfos = arrayOf<SnakeInfo>(SnakeInfo("a", 3, "a", intArrayOf(8, 7, 4, 1), 0), SnakeInfo("b", 8, "b", intArrayOf(3, 0), 0))

        val map = createMap(snakeInfos, intArrayOf(), intArrayOf())

        val mapUtil = MapUtil(map, "a")

        val snakeSpread = mapUtil.getSnakeSpread("a")

        assertEquals(4, snakeSpread.size)

        val head = mapUtil.getTileAt(snakeSpread[0]) as SnakeHeadTile
        assertEquals("a", head.playerId)

        (1..3).forEach { pos ->
            val body = mapUtil.getTileAt(snakeSpread[pos]) as SnakeBodyTile
            assertEquals("a", body.playerId)

            if (pos < 3) {
                assertFalse(body.tail)
            } else {
                assertTrue(body.tail)
            }
        }
    }

    @Test
    fun testTranslateCoordinateWithNegativeX() {
        val snakeInfos = arrayOf<SnakeInfo>(SnakeInfo("a", 3, "a", intArrayOf(8, 7, 4, 1), 0), SnakeInfo("b", 8, "b", intArrayOf(3, 0), 0))
        val map = createMap(snakeInfos, intArrayOf(), intArrayOf())
        val mapUtil = MapUtil(map, "a")

        assertThrows(RuntimeException::class.java) {
            mapUtil.getTileAt(MapCoordinate(-1, 0))
        }

    }

    @Test
    @Throws(Exception::class)
    fun testListCoordinatesContainingObstacle() {
        val mapUtil = MapUtil(createMap(
                arrayOf<SnakeInfo>(), intArrayOf(3, 0, 2), intArrayOf(8, 1)), "a")
        val obstacles = mapUtil.listCoordinatesContainingObstacle()

        assertEquals(2, obstacles.size)

        obstacles.forEach { it -> assertTrue(mapUtil.getTileAt(it) is ObstacleTile) }
    }

    @Test
    @Throws(Exception::class)
    fun testListCoordinatesContainingFood() {
        val mapUtil = MapUtil(createMap(
                arrayOf<SnakeInfo>(), intArrayOf(3, 0, 2), IntArray(0)), "a")

        val foods = mapUtil.listCoordinatesContainingFood()

        assertEquals(3, foods.size)
        foods.forEach { it -> assertTrue(mapUtil.getTileAt(it) is FoodTile)  }
    }

    @Test
    @Throws(Exception::class)
    fun testTranslateCoordinates() {
        val mapUtil = MapUtil(createMap(
                arrayOf<SnakeInfo>(), IntArray(0), IntArray(0)
        ), "a")

        val coordinates = arrayOf(MapCoordinate(0, 0), MapCoordinate(1, 1), MapCoordinate(2, 2))

        assertArrayEquals(intArrayOf(0, 4, 8),
                mapUtil.translateCoordinates(coordinates))
    }

    @Test
    @Throws(Exception::class)
    fun testTranslateCoordinateLarge() {
        val map = Map(50, 50, 8, arrayOf<SnakeInfo>(), IntArray(0), IntArray(0))
        val mapUtil = MapUtil(map, "a")

        println(mapUtil.translatePosition(637))
        println(mapUtil.translatePosition(687))
    }

    @Test
    @Throws(Exception::class)
    fun testGetPlayerLength() {

        val snakeInfos = arrayOf<SnakeInfo>(SnakeInfo("a", 3, "a", intArrayOf(8, 7, 4, 1), 0), SnakeInfo("b", 8, "b", intArrayOf(3, 0), 0))

        val map = createMap(snakeInfos, intArrayOf(), intArrayOf())
        val mapUtil = MapUtil(map, "a")

        assertEquals(4, mapUtil.getPlayerLength("a"))
        assertEquals(2, mapUtil.getPlayerLength("b"))
    }

    @Test
    fun testGetMyPosition() {
        val snakeInfos = arrayOf<SnakeInfo>(SnakeInfo("a", 3, "a", intArrayOf(8, 7, 4, 1), 0), SnakeInfo("b", 8, "b", intArrayOf(3, 0), 0))

        val snakePosition = MapCoordinate(2, 2)

        val map = createMap(snakeInfos, intArrayOf(), intArrayOf())
        val mapUtil = MapUtil(map, "a")
        assertEquals(snakePosition, mapUtil.myPosition)
    }

    /*
        0 1 2
        3 4 5
        6 7 8
    */
    private fun createMap(snakeInfos: Array<SnakeInfo>, foods: IntArray, obstacles: IntArray): Map {
        return Map(3, 3, 8, snakeInfos, foods, obstacles)
    }


}