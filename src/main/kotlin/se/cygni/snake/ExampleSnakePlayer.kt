package se.cygni.snake

import org.slf4j.LoggerFactory
import se.cygni.snake.client.BaseSnakeClient
import se.cygni.snake.client.MapUtil
import se.cygni.snake.client.api.*
import se.cygni.snake.client.api.model.GameMode
import se.cygni.snake.client.api.model.SnakeDirection
import java.util.*


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
    }


    val thread = Thread(task)
    thread.start()
}

class ExampleSnakePlayer : BaseSnakeClient() {
    private val LOG by lazyLogger()
    private var random = Random()

    override val name = "#horv_" + random.nextInt(1000)
    //    private String host = "localhost";
    //    private int port = 8080;
    //    override val serverHost = "localhost"
    //    override val serverPort = 8080

    override val serverHost = "snake.cygni.se"
    override val serverPort = 80
    override val gameMode = GameMode.TRAINING

    var lastDirection: SnakeDirection

    private fun randomDirection() = SnakeDirection.values()[random.nextInt(4)]

    init {
        lastDirection = randomDirection()
    }

    override fun onMapUpdate(mapUpdateEvent: MapUpdateEvent) {

        // MapUtil contains lot's of useful methods for querying the map!
        val mapUtil = MapUtil(mapUpdateEvent.map, playerId)


        var chosenDirection = lastDirection
        val directions = mutableListOf<SnakeDirection>()


        if (!mapUtil.canIMoveInDirection(lastDirection)) {
            // Let's see in which directions I can move
            if (mapUtil.canIMoveInDirection(SnakeDirection.LEFT))
                directions.add(SnakeDirection.LEFT)
            if (mapUtil.canIMoveInDirection(SnakeDirection.RIGHT))
                directions.add(SnakeDirection.RIGHT)
            if (mapUtil.canIMoveInDirection(SnakeDirection.UP))
                directions.add(SnakeDirection.UP)
            if (mapUtil.canIMoveInDirection(SnakeDirection.DOWN))
                directions.add(SnakeDirection.DOWN)

            // Choose a random direction
            if (!directions.isEmpty())
                chosenDirection = directions.get(random.nextInt(directions.size))
        }

        // Register action here!
        registerMove(mapUpdateEvent.gameTick, chosenDirection)

        lastDirection = chosenDirection
    }

    override fun onInvalidPlayerName(invalidPlayerName: InvalidPlayerName) {

    }

    override fun onGameResult(gameResultEvent: GameResultEvent) {
        LOG.info("Got a Game result:")
        gameResultEvent.playerRanks.forEach { LOG.info(it.toString()) }
    }

    override fun onSnakeDead(snakeDeadEvent: SnakeDeadEvent) {
        LOG.info("A snake ${snakeDeadEvent.playerId} died by ${snakeDeadEvent.deathReason} at tick: ${snakeDeadEvent.gameTick}")
    }

    override fun onTournamentEnded(tournamentEndedEvent: TournamentEndedEvent) {
        LOG.info("Tournament has ended, winner playerId: {}", tournamentEndedEvent.playerWinnerId)
        tournamentEndedEvent.gameResult.forEachIndexed { index, pp ->
            LOG.info("${index}. ${pp.name} - ${pp.points} points")
        }
    }

    override fun onGameEnded(gameEndedEvent: GameEndedEvent) {
        LOG.info("${name} GameEnded gameId: ${gameEndedEvent.gameId}, at tick: ${gameEndedEvent.gameTick}, winner: ${gameEndedEvent.playerWinnerId}")
    }

    override fun onGameStarting(gameStartingEvent: GameStartingEvent) {
        LOG.info("GameStartingEvent, gameId: ${gameStartingEvent.gameId}")
    }

    override fun onPlayerRegistered(playerRegistered: PlayerRegistered) {
        LOG.info("PlayerRegistered: $playerRegistered")

        // Disable this if you want to start the game manually from
        // the web GUI
        startGame()
    }

    override fun onGameLink(gameLinkEvent: GameLinkEvent) {
        LOG.info("The game can be viewed at: ${gameLinkEvent.url}")
    }

    override fun onSessionClosed() {
        LOG.info("Session closed")
    }

    override fun onConnected() {
        LOG.info("Connected as: ${name}, registering for ${gameMode}...")
        val gameSettings = trainingWorld()
        gameSettings.startObstacles = 10
        registerForGame(gameSettings)
    }
}