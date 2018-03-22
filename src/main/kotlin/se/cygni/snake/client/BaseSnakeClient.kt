package se.cygni.snake.client

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import se.cygni.snake.client.api.*
import se.cygni.snake.lazyLogger
import java.util.concurrent.TimeUnit

class BaseSnakeClient: WebSocketListener() {
    val LOG by lazyLogger()
    var socket: WebSocket? = null


    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        LOG.info("Connected to server")
        this.socket = webSocket
        sendHeartbeat()
        sendClientInfo()
    }

    private fun sendClientInfo() {
        val clientInfo = ClientInfo(
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
        LOG.info("Sending: $encodedMessage")
        socket?.send(encodedMessage)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String?) {
        super.onClosing(webSocket, code, reason)
        LOG.info("Closing with code: $code and for reason $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        LOG.info("Failure ${t.message} $response")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        LOG.info("Message $text")

        try {
            val decoded = text.decode()
            LOG.info("Decoded: $decoded")
        } catch (e: Exception) {
            LOG.info("Error $e")
        }


    }

    private fun sendHeartbeat() {
        launch {
            delay(5, TimeUnit.SECONDS)
            LOG.info("Sending heartbeat")
            sendMessage(HeartBeatRequest(""))
        }
    }

}