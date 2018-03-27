package se.cygni.snake

import org.slf4j.LoggerFactory
import se.cygni.snake.client.BaseSnakeClient
import se.cygni.snake.client.MapUtil
import se.cygni.snake.client.api.*
import se.cygni.snake.client.api.model.GameMode
import se.cygni.snake.client.api.model.SnakeDirection
import java.util.*
import kotlin.system.exitProcess


fun main(args: Array<String>) {
    val LOG = LoggerFactory.getLogger("Main")
    val task = {
        val sp = ExampleSnakePlayer()
        sp.connect()

        // Keep this process alive as long as the
        // Snake is connected and playing.
        do {
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        } while (sp.isPlaying())

        LOG.info("Shutting down")
        exitProcess(0)
    }


    val thread = Thread(task)
    thread.start()
}

class ExampleSnakePlayer : BaseSnakeClient() {
    private val LOG by lazyLogger()

    // Set to false if you want to start the game from a GUI
    private val AUTO_START_GAME = true

    // Name of your snake
    override val name = "HorvSnake9001"

    override val HOST = "localhost"
    override val PORT = 8080
    //override val HOST = "snake.cygni.se"
    //override val PORT = 80

    override val GAME_MODE = GameMode.TRAINING


    override fun onMapUpdate(mapUpdateEvent: MapUpdateEvent) {

        // MapUtil contains lot's of useful methods for querying the map!
        val mapUtil = MapUtil(mapUpdateEvent.map, playerId)

        val directions = mutableListOf<SnakeDirection>()

        // Let's see in which directions I can move
        for (direction in SnakeDirection.values()) {
            if (mapUtil.canIMoveInDirection(direction)) {
                directions.add(direction)
            }
        }

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