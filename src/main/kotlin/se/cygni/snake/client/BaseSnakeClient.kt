package se.cygni.snake.client

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import okhttp3.*
import org.slf4j.LoggerFactory
import se.cygni.snake.client.api.*
import se.cygni.snake.client.api.model.GameMode
import se.cygni.snake.client.api.model.GameSettings
import se.cygni.snake.client.api.model.SnakeDirection
import se.cygni.snake.lazyLogger
import java.util.concurrent.TimeUnit

val LOG = LoggerFactory.getLogger(BaseSnakeClient::class.java)

abstract class BaseSnakeClient: WebSocketListener(), SnakeClient  {

    private var socket: WebSocket? = null
    var playerId: String = ""
    private var lastGameId: String = ""
    private var gameEnded: Boolean = false
    private var tournamentEnded: Boolean = false

    fun connect() {
        val uri = String.format("ws://%s:%d/%s", serverHost, serverPort, gameMode.toString().toLowerCase())

        val okHttpClient = OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build()

        val request = Request.Builder()
                .url(uri)
                .build()

        LOG.info("Connecting to ${uri}...")
        socket = okHttpClient.newWebSocket(request, this)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        LOG.info("Connected to server")
        this.socket = webSocket
        this.onConnected()
        scheduleHeartbeat()
    }

    fun registerForGame(gameSettings: GameSettings) {
        LOG.info("Register for game...")
        val registerPlayer = RegisterPlayer(
                playerName= name,
                gameSettings = gameSettings
        )
        sendMessage(registerPlayer)
    }

    fun startGame() {
        LOG.info("Starting game...")
        val startGame = StartGame(playerId)
        sendMessage(startGame)
    }

    fun registerMove(gameTick: Long, direction: SnakeDirection) {
        val registerMove = RegisterMove(playerId, lastGameId, gameTick, direction)
        sendMessage(registerMove)
    }

    fun isPlaying(): Boolean {
        return if (gameMode === GameMode.TRAINING) {
            socket != null && !gameEnded
        } else {
            socket != null && !tournamentEnded
        }
    }

    private fun sendClientInfo() {
        val clientInfo = ClientInfo( // TODO non-hardcoded languageVersion-version + OS
                language = "Kotlin",
                languageVersion = "Unknown",
                operatingSystem = "Ubuntu",
                operatingSystemVersion = "",
                clientVersion = "0.1"
        )

        sendMessage(clientInfo)
    }

    private fun sendMessage(message: GameMessage) {
        val encodedMessage = message.encode()
        LOG.debug("Sending: $encodedMessage")
        socket?.send(encodedMessage)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        LOG.info("Closing with code: $code and for reason $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        LOG.info("Failure ${t.message} $response")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        LOG.debug("Message $text")
        var decoded: GameMessage? = null;
        try {
            decoded = text.decode()
            LOG.debug("Decoded: $decoded")
        } catch (e: Exception) {
            LOG.info("Error when decoding $e")
            e.printStackTrace()
        }

        decoded?.let(this::handleMessage)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        LOG.warn("Server connection closed")
        onSessionClosed()
    }

    private fun handleMessage(message: GameMessage) {
        when(message) {
            is PlayerRegistered -> {
                onPlayerRegistered(message)
                playerId = message.receivingPlayerId
                sendClientInfo()
            }
            is MapUpdateEvent -> {
                lastGameId = message.gameId
                onMapUpdate(message)
            }
            is GameStartingEvent -> this.onGameStarting(message)
            is SnakeDeadEvent -> this.onSnakeDead(message)
            is GameResultEvent -> this.onGameResult(message)
            is GameEndedEvent -> {
                this.onGameEnded(message)
                gameEnded = true
            }
            is TournamentEndedEvent -> {
                this.onTournamentEnded(message)
                tournamentEnded = true
            }
            is InvalidPlayerName -> this.onInvalidPlayerName(message)
            is GameLinkEvent -> this.onGameLink(message)
            is HeartBeatResponse -> scheduleHeartbeat()
            is InvalidMessage -> {
                LOG.error("Server did not understand my last message")
                LOG.error("Message sent: ${message.receivedMessage}")
                LOG.error("Error message: ${message.errorMessage}")
            }
        }
    }

    private fun scheduleHeartbeat() {
        launch {
            delay(30, TimeUnit.SECONDS)
            LOG.debug("Sending heartbeat")
            sendMessage(HeartBeatRequest())
        }
    }

}