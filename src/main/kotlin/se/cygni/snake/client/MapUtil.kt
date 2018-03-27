package se.cygni.snake.client

import se.cygni.snake.client.api.model.EmptyTile
import se.cygni.snake.client.api.model.FoodTile
import se.cygni.snake.client.api.model.Map
import se.cygni.snake.client.api.model.ObstacleTile
import se.cygni.snake.client.api.model.SnakeBodyTile
import se.cygni.snake.client.api.model.SnakeDirection
import se.cygni.snake.client.api.model.SnakeHeadTile
import se.cygni.snake.client.api.model.SnakeInfo
import se.cygni.snake.client.api.model.Tile

import java.util.Arrays
import java.util.BitSet
import java.util.HashMap

class MapUtil(private val map: Map, private val playerId: String) {
    private val mapSize: Int = map.height * map.width
    private val snakeInfoMap: MutableMap<String, SnakeInfo> = HashMap()
    private val snakeSpread: MutableMap<String, BitSet> = HashMap()
    private val foods: BitSet = BitSet(mapSize)
    private val obstacles: BitSet = BitSet(mapSize)
    private val snakes: BitSet = BitSet(mapSize)

    /**
     * @return The MapCoordinate of your snake's head.
     */
    val myPosition: MapCoordinate
        get() = translatePosition(
                snakeInfoMap[playerId]!!.positions[0])

    init {
        populateSnakeInfo()
        populateStaticTileBits()
    }

    fun canIMoveInDirection(direction: SnakeDirection): Boolean {
        return try {
            val myPos = myPosition
            val myNewPos = myPos.translateByDirection(direction)

            isTileAvailableForMovementTo(myNewPos)
        } catch (e: Exception) {
            false
        }

    }

    /**
     * Returns an array of MapCoordinate for the snake with the
     * supplied playerId.
     *
     *
     * The first MapCoordinate always points to the MapSnakeHead and
     * the last to the snakes MapSnakeBody tail part.
     *
     * @param playerId
     * @return an array of MapCoordinate for the snake with matching playerId
     */
    fun getSnakeSpread(playerId: String): Array<MapCoordinate> {
        return translatePositions(
                snakeInfoMap[playerId]!!.positions)
    }

    fun getPlayerLength(playerId: String): Int {
        return snakeInfoMap[playerId]!!.length
    }

    /**
     * @return An array containing all MapCoordinates where there's Food
     */
    fun listCoordinatesContainingFood(): Array<MapCoordinate> {
        return translatePositions(map.foodPositions)
    }

    /**
     * @return An array containing all MapCoordinates where there's an Obstacle
     */
    fun listCoordinatesContainingObstacle(): Array<MapCoordinate> {
        return translatePositions(map.obstaclePositions)
    }

    /**
     * @param coordinate
     * @return true if the TileContent at coordinate is Empty or contains Food
     */
    fun isTileAvailableForMovementTo(coordinate: MapCoordinate): Boolean {
        if (isCoordinateOutOfBounds(coordinate))
            return false

        val position = translateCoordinate(coordinate)
        return isTileAvailableForMovementTo(position)
    }

    /**
     * @param position map position
     * @return true if the TileContent at map position is Empty or contains Food
     */
    private fun isTileAvailableForMovementTo(position: Int): Boolean {
        return if (isPositionOutOfBounds(position)) false else !(obstacles.get(position) || snakes.get(position))

    }

    /**
     * @param coordinate map coordinate
     * @return whether or not it is out of bounds
     */
    fun isCoordinateOutOfBounds(coordinate: MapCoordinate): Boolean {
        return coordinate.x < 0 || coordinate.x >= map.width || coordinate.y < 0 || coordinate
                .y >= map.height
    }

    /**
     * @param position map position
     * @return whether or not it is out of bounds
     */
    private fun isPositionOutOfBounds(position: Int): Boolean {
        return position < 0 || position >= mapSize
    }

    /**
     * @param position map position
     * @return the TileContent at the specified position of the flattened map.
     */
    private fun getTileAt(position: Int): Tile {
        if (isPositionOutOfBounds(position)) {
            val errorMessage = String.format("Position [%s] is out of bounds", position)
            throw RuntimeException(errorMessage)
        }

        if (foods.get(position)) {
            return FoodTile()
        }

        if (obstacles.get(position)) {
            return ObstacleTile()
        }

        return if (snakes.get(position)) {
            getSnakePart(position)
        } else EmptyTile()

    }

    /**
     * @param coordinate
     * @return the TileContent at the specified coordinate
     */
    fun getTileAt(coordinate: MapCoordinate): Tile {
        return getTileAt(translateCoordinate(coordinate))
    }

    /**
     * Converts a position in the flattened single array representation
     * of the Map to a MapCoordinate.
     *
     * @param position
     * @return
     */
    fun translatePosition(position: Int): MapCoordinate {
        val y = position / map.width
        val x = position - y * map.width
        return MapCoordinate(x, y)
    }

    /**
     * Converts a MapCoordinate to the same position in the flattened
     * single array representation of the Map.
     *
     * @param coordinate
     * @return
     */
    fun translateCoordinate(coordinate: MapCoordinate): Int {
        if (isCoordinateOutOfBounds(coordinate)) {
            throw RuntimeException("Coordinate [${coordinate.x},${coordinate.y}] is out of bounds")
        }

        return coordinate.x + coordinate.y * map.width
    }

    fun translatePositions(positions: IntArray): Array<MapCoordinate> {
        return Arrays.stream(positions)
                .mapToObj { pos -> translatePosition(pos) }
                .toArray<MapCoordinate> { l -> arrayOfNulls(l) }
    }

    fun translateCoordinates(coordinates: Array<MapCoordinate>): IntArray {
        return Arrays.stream(coordinates)
                .mapToInt { coordinate -> translateCoordinate(coordinate) }
                .toArray()
    }

    private fun getSnakePart(position: Int): Tile {
        val playerId = getPlayerIdAtPosition(position)
        val snakeInfo = snakeInfoMap[playerId]
        val order = snakeInfo!!.positions.indexOf(position)

        return when(order) {
            0 -> SnakeHeadTile(snakeInfo.name, playerId)
            snakeInfo.length - 1 -> SnakeBodyTile(true, playerId, order)
            else -> SnakeBodyTile(false, playerId, order)
        }
    }

    private fun getPlayerIdAtPosition(position: Int): String {
        val info = map.snakeInfos.find { snakeSpread[it.id]!!.get(position) }
        return info?.id ?: throw RuntimeException("No snake at position: $position")
    }

    private fun populateSnakeInfo() {
        map.snakeInfos.forEach { snakeInfo ->
            snakeInfoMap[snakeInfo.id] = snakeInfo

            val snakePositions = BitSet(mapSize)
            snakeInfo.positions.forEach { pos ->
                snakes.set(pos)
                snakePositions.set(pos)
            }

            snakeSpread[snakeInfo.id] = snakePositions
        }
    }

    private fun populateStaticTileBits() {
        map.foodPositions.forEach(foods::set)
        map.obstaclePositions.forEach(obstacles::set)
    }
}