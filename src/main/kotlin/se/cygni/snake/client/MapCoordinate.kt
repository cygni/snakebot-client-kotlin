package se.cygni.snake.client

import se.cygni.snake.client.api.model.SnakeDirection

data class MapCoordinate(val x: Int, val y: Int) {

    fun translateByDirection(direction: SnakeDirection): MapCoordinate {
        return when (direction) {
            SnakeDirection.DOWN -> translateBy(0, 1)
            SnakeDirection.UP -> translateBy(0, -1)
            SnakeDirection.LEFT -> translateBy(-1, 0)
            SnakeDirection.RIGHT -> translateBy(1, 0)
        }
    }

    fun translateBy(deltaX: Int, deltaY: Int): MapCoordinate {
        return MapCoordinate(x + deltaX, y + deltaY)
    }

    fun getManhattanDistanceTo(coordinate: MapCoordinate): Int {
        return Math.abs(x - coordinate.x) + Math.abs(y - coordinate.y)
    }
}