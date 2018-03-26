package se.cygni.snake.client.api.model

import com.fasterxml.jackson.annotation.JsonIgnore

class SnakeInfo(
        val name: String,
        val points: Int,
        val id: String,
        val positions: IntArray,
        val tailProtectedForGameTicks: Int
) {
    val length: Int
        @JsonIgnore
        get() = positions.size

    val isAlive: Boolean
        @JsonIgnore
        get() = length > 0
}
