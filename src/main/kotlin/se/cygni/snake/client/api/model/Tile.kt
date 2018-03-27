package se.cygni.snake.client.api.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import se.cygni.snake.client.api.TileDeserializer

enum class TileType(val type: String) {
    EMPTY("empty"),
    FOOD("food"),
    OBSTACLE("obstacle"),
    SNAKE_BODY("snakebody"),
    SNAKE_HEAD("snakehead");

    companion object {
        fun from(type: String): TileType = TileType.values().first { it.type == type }
    }
}


@JsonDeserialize(using = TileDeserializer::class)
sealed class Tile(
        @JsonIgnore val tileType: TileType,
        @JsonIgnore val display: String) {
    fun content() = { tileType.type }
}

class EmptyTile: Tile(TileType.EMPTY, " ")
class FoodTile: Tile(TileType.FOOD, "F")
class ObstacleTile: Tile(TileType.OBSTACLE, "0")

data class SnakeBodyTile(val tail: Boolean,
                         val playerId: String,
                         val order: Int
): Tile(TileType.SNAKE_BODY, "$order")

data class SnakeHeadTile(val name: String,
                         val playerId: String
): Tile(TileType.SNAKE_HEAD, "SH")
