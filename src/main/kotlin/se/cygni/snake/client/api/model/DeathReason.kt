package se.cygni.snake.client.api.model

enum class DeathReason {
    CollisionWithWall,
    CollisionWithObstacle,
    CollisionWithSnake,
    CollisionWithSelf
}