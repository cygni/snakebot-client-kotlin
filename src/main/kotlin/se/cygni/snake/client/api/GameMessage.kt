package se.cygni.snake.client.api

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import se.cygni.snake.client.api.model.GameMode
import se.cygni.snake.client.api.model.GameSettings
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.sun.org.apache.xerces.internal.util.DOMUtil.getParent





val GAME_ENDED = "se.cygni.snake.api.event.GameEndedEvent"
val TOURNAMENT_ENDED = "se.cygni.snake.api.event.TournamentEndedEvent"
val MAP_UPDATE = "se.cygni.snake.api.event.MapUpdateEvent"
val SNAKE_DEAD = "se.cygni.snake.api.event.SnakeDeadEvent"
val GAME_STARTING = "se.cygni.snake.api.event.GameStartingEvent"
val PLAYER_REGISTERED = "se.cygni.snake.api.response.PlayerRegistered"
val INVALID_PLAYER_NAME = "se.cygni.snake.api.exception.InvalidPlayerName"
val HEART_BEAT_RESPONSE = "se.cygni.snake.api.response.HeartBeatResponse"
val GAME_LINK_EVENT = "se.cygni.snake.api.event.GameLinkEvent"
val GAME_RESULT_EVENT = "se.cygni.snake.api.event.GameResultEvent"

val REGISTER_PLAYER_MESSAGE_TYPE = "se.cygni.snake.api.request.RegisterPlayer"
val START_GAME = "se.cygni.snake.api.request.StartGame"
val REGISTER_MOVE = "se.cygni.snake.api.request.RegisterMove"
val HEART_BEAT_REQUEST = "se.cygni.snake.api.request.HeartBeatRequest"
val CLIENT_INFO = "se.cygni.snake.api.request.ClientInfo"

sealed class GameMessage(val type: String)

val mapper = jacksonObjectMapper()


fun getCustomMapper(): ObjectMapper {
    val module: SimpleModule = SimpleModule("custom", Version.unknownVersion())
    module.addDeserializer(GameMessage::class.java, Deserializer())
    val jacksonObjectMapper = jacksonObjectMapper()
    jacksonObjectMapper.registerModule(module)
    return jacksonObjectMapper
}

class Deserializer: JsonDeserializer<GameMessage>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): GameMessage {
        val node: JsonNode = p.getCodec().readTree(p)
        val type: String = node.get("type").asText()

        return when(type) {
            HEART_BEAT_RESPONSE -> mapper.treeToValue<HeartBeatRespons>(node, HeartBeatRespons::class.java) as GameMessage
            else -> throw RuntimeException("Failed to deserialize")
        }

    }

}

fun GameMessage.encode(): String = mapper.writeValueAsString(this)
fun String.decode(): GameMessage = getCustomMapper().readValue(this)

data class PlayerRegisterd(
        val gameId: String,
        val name: String,
        val gameSettings: GameSettings,
        val gameMode: GameMode
): GameMessage(PLAYER_REGISTERED)

data class HeartBeatRequest(
        val receivingPlayerId: String
): GameMessage(HEART_BEAT_REQUEST)

data class HeartBeatRespons(
        val receivingPlayerId: String,
        val timestamp: Long
): GameMessage(HEART_BEAT_RESPONSE)

data class RegisterPlayer(
        val playerName: String,
        val gameSettings: GameSettings
): GameMessage(REGISTER_PLAYER_MESSAGE_TYPE)

data class ClientInfo(
        val language: String,
        val languageVersion: String,
        val operatingSystem: String,
        val operatingSystemVersion: String,
        val clientVersion: String
): GameMessage(CLIENT_INFO)