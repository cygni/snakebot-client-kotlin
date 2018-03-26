package se.cygni.snake.client.api.model

class Map(
        val width: Int,
        val height: Int,
        val worldTick: Long,
        val snakeInfos: Array<SnakeInfo>,
        // List of positions containing Food
        val foodPositions: IntArray,
        // List of positions containing Obstacle
        val obstaclePositions: IntArray
)
