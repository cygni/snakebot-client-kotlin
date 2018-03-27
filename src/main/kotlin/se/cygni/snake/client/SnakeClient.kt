package se.cygni.snake.client

import se.cygni.snake.client.api.*
import se.cygni.snake.client.api.model.GameMode

interface SnakeClient {

    val HOST: String
    val PORT: Int
    val GAME_MODE: GameMode
    val name: String

    fun onMapUpdate(mapUpdateEvent: MapUpdateEvent)

    fun onSnakeDead(snakeDeadEvent: SnakeDeadEvent)

    fun onGameResult(gameResultEvent: GameResultEvent)

    fun onGameEnded(gameEndedEvent: GameEndedEvent)

    fun onGameStarting(gameStartingEvent: GameStartingEvent)

    fun onTournamentEnded(tournamentEndedEvent: TournamentEndedEvent)

    fun onPlayerRegistered(playerRegistered: PlayerRegistered)

    fun onInvalidPlayerName(invalidPlayerName: InvalidPlayerName)

    fun onGameLink(gameLinkEvent: GameLinkEvent)

    fun onConnected()

    fun onSessionClosed()

}
