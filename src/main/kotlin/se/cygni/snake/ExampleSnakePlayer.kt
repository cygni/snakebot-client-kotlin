package se.cygni.snake

import se.cygni.snake.client.BaseSnakeClient
import se.cygni.snake.client.MapUtil
import se.cygni.snake.client.api.*
import se.cygni.snake.client.api.model.GameMode
import se.cygni.snake.client.api.model.SnakeDirection
import java.util.*


class ExampleSnakePlayer : BaseSnakeClient() {
    private val LOG by lazyLogger()

    // Set to false if you want to start the game from a GUI
    private val AUTO_START_GAME = true

    // Name of your snake
    override val name = "HorvSnake9001"

    //override val HOST = "localhost"
    //override val PORT = 8080
    override val HOST = "snake.cygni.se"
    override val PORT = 80

    override val GAME_MODE = GameMode.TRAINING


    override fun onMapUpdate(mapUpdateEvent: MapUpdateEvent) {
        // Input your implementation here, below is a dummy example implementation that
        // goes into a random valid direction at every step


        // MapUtil contains lot's of useful methods for querying the map!
        val mapUtil = MapUtil(mapUpdateEvent.map, playerId)

        // Let's see in which directions I can move
        val directions = SnakeDirection.values().filter(mapUtil::canIMoveInDirection)

        val r = Random()
        var chosenDirection = SnakeDirection.DOWN
        // Choose a random direction
        if (!directions.isEmpty()) {
            chosenDirection = directions[r.nextInt(directions.size)]
        }

        // Register action here!
        registerMove(mapUpdateEvent.gameTick, chosenDirection)
    }


    override fun onInvalidPlayerName(invalidPlayerName: InvalidPlayerName) {
        LOG.debug("InvalidPlayerNameEvent: $invalidPlayerName")
    }

    override fun onSnakeDead(snakeDeadEvent: SnakeDeadEvent) {
        LOG.info("A snake {} died by {}",
                snakeDeadEvent.playerId,
                snakeDeadEvent.deathReason)
    }

    override fun onGameResult(gameResultEvent: GameResultEvent) {
        LOG.info("Game result:")
        gameResultEvent.playerRanks.forEach { playerRank -> LOG.info(playerRank.toString()) }
    }

    override fun onGameEnded(gameEndedEvent: GameEndedEvent) {
        LOG.debug("GameEndedEvent: $gameEndedEvent")
    }

    override fun onGameStarting(gameStartingEvent: GameStartingEvent) {
        LOG.debug("GameStartingEvent: $gameStartingEvent")
    }

    override fun onPlayerRegistered(playerRegistered: PlayerRegistered) {
        LOG.info("PlayerRegistered: $playerRegistered")

        if (AUTO_START_GAME) {
            startGame()
        }
    }

    override fun onTournamentEnded(tournamentEndedEvent: TournamentEndedEvent) {
        LOG.info("Tournament has ended, winner playerId: {}", tournamentEndedEvent.playerWinnerId)
        var c = 1
        for (pp in tournamentEndedEvent.gameResult) {
            LOG.info("{}. {} - {} points", c++, pp.name, pp.points)
        }
    }

    override fun onGameLink(gameLinkEvent: GameLinkEvent) {
        LOG.info("The game can be viewed at: {}", gameLinkEvent.url)
    }

    override fun onSessionClosed() {
        LOG.info("Session closed")
    }

    override fun onConnected() {
        LOG.info("Connected, registering for training...")
        val gameSettings = trainingWorld()
        registerForGame(gameSettings)
    }

}