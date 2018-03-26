package se.cygni.snake.client.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import se.cygni.snake.client.api.model.*
import se.cygni.snake.client.api.model.Map
import kotlin.reflect.KClass

enum class GameMessageType(val type: String, val klass: KClass<out GameMessage>) {
    HEARTBEAT_REQUEST("se.cygni.snake.api.request.HeartBeatRequest", HeartBeatRequest::class),
    HEART_BEAT_RESPONSE("se.cygni.snake.api.response.HeartBeatResponse", HeartBeatResponse::class),
    PLAYER_REGISTERED("se.cygni.snake.api.response.PlayerRegistered", PlayerRegistered::class),
    REGISTER_PLAYER("se.cygni.snake.api.request.RegisterPlayer", RegisterPlayer::class),
    CLIENT_INFO("se.cygni.snake.api.request.ClientInfo", ClientInfo::class),
    MAP_UPDATE_EVENT("se.cygni.snake.api.event.MapUpdateEvent", MapUpdateEvent::class),
    GAME_STARTING_EVENT("se.cygni.snake.api.event.GameStartingEvent", GameStartingEvent::class),
    SNAKE_DEAD_EVENT("se.cygni.snake.api.event.SnakeDeadEvent", SnakeDeadEvent::class),
    GAME_RESULT_EVENT("se.cygni.snake.api.event.GameResultEvent", GameResultEvent::class),
    GAME_ENDED_EVENT("se.cygni.snake.api.event.GameEndedEvent", GameEndedEvent::class),
    TOURNAMENT_ENDED_EVENT("se.cygni.snake.api.event.TournamentEndedEvent", TournamentEndedEvent::class),
    INVALID_PLAYER_NAME("se.cygni.snake.api.exception.InvalidPlayerName", InvalidPlayerName::class),
    GAME_LINK_EVENT("se.cygni.snake.api.event.GameLinkEvent", GameLinkEvent::class),
    INVALID_MESSAGE("se.cygni.snake.api.exception.InvalidMessage", InvalidMessage::class),
    REGISTER_MOVE("se.cygni.snake.api.request.RegisterMove", RegisterMove::class),
    START_GAME("se.cygni.snake.api.request.StartGame", StartGame::class);

    companion object {
        fun fromTypeString(s: String): GameMessageType = GameMessageType.values().first { it.type == s }
        fun fromClass(klass: KClass<out GameMessage>): GameMessageType = GameMessageType.values().first { it.klass == klass }
    }

}

@JsonIgnoreProperties("type")
sealed class GameMessage{
    abstract val receivingPlayerId: String
    val timestamp: Long = System.currentTimeMillis()
}

data class PlayerRegistered(
        override val receivingPlayerId: String,
        val gameId: String,
        val name: String,
        val gameSettings: GameSettings,
        val gameMode: GameMode
): GameMessage()

data class HeartBeatRequest(
        override val receivingPlayerId: String = ""
): GameMessage()

data class HeartBeatResponse(
        override val receivingPlayerId: String
): GameMessage()

data class RegisterPlayer(
        override val receivingPlayerId: String = "",
        val playerName: String,
        val gameSettings: GameSettings
): GameMessage()

data class ClientInfo(
        override val receivingPlayerId: String = "",
        val language: String,
        val languageVersion: String,
        val operatingSystem: String,
        val operatingSystemVersion: String,
        val clientVersion: String
): GameMessage()

data class MapUpdateEvent(
        override val receivingPlayerId: String,
        val gameTick: Long,
        val gameId: String,
        val map: Map
): GameMessage()

data class GameStartingEvent(
        override val receivingPlayerId: String,
        val gameId: String,
        val noofPlayers: Int,
        val width: Int,
        val height: Int,
        val gameSettings: GameSettings
): GameMessage()

data class SnakeDeadEvent(
        override val receivingPlayerId: String,
        val deathReason: DeathReason,
        val playerId: String,
        val x: Int,
        val y: Int,
        val gameId: String,
        val gameTick: Long
):GameMessage()

data class GameResultEvent(
        override val receivingPlayerId: String,
        val gameId: String,
        val playerRanks: List<PlayerRank>
): GameMessage()

data class GameEndedEvent(
        override val receivingPlayerId: String,
        val playerWinnerId: String,
        val playerWinnerName: String,
        val gameId: String,
        val gameTick: Long,
        val map: Map
): GameMessage()

data class TournamentEndedEvent(
        override val receivingPlayerId: String,
        val playerWinnerId: String,
        val gameId: String,
        val gameResult: List<PlayerPoints>,
        val tournamentName: String,
        val tournamentId: String
): GameMessage()

class InvalidPlayerName(
        override val receivingPlayerId: String,
        val reasonCode: PlayerNameInvalidReason
): GameMessage() {
    enum class PlayerNameInvalidReason {
        Taken,
        Empty,
        InvalidCharacter
    }
}

data class GameLinkEvent(
        override val receivingPlayerId: String,
        val gameId: String,
        val url: String
): GameMessage()

data class InvalidMessage(
        override val receivingPlayerId: String,
        val errorMessage: String,
        val receivedMessage: String
): GameMessage()

data class RegisterMove(
        override val receivingPlayerId: String,
        val gameId: String,
        val gameTick: Long,
        val direction: SnakeDirection
) : GameMessage()


data class StartGame(
        override val receivingPlayerId: String
): GameMessage()



