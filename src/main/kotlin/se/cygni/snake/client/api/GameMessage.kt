package se.cygni.snake.client.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import se.cygni.snake.client.api.model.GameMode
import se.cygni.snake.client.api.model.GameSettings

sealed class GameMessage {
    abstract val receivingPlayerId: String
    abstract val timestamp: Long
}

val mapper = jacksonObjectMapper()

fun GameMessage.encode(): String = mapper.writeValueAsBytes(this).toString()
fun String.decode(): GameMessage = mapper.readValue(this)

data class PlayerRegisterd(
        override val receivingPlayerId: String,
        override val timestamp: Long,
        val gameId: String,
        val name: String,
        val gameSettings: GameSettings,
        val gameMode: GameMode
): GameMessage()

data class HeartBeatResponse(
        override val receivingPlayerId: String,
        override val timestamp: Long
): GameMessage()

data class RegisterPlayer(
        override val receivingPlayerId: String,
        override val timestamp: Long,
        val playerName: String,
        val gameSettings: GameSettings
): GameMessage()