package se.cygni.snake.client

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import se.cygni.snake.lazyLogger
import java.util.concurrent.TimeUnit

class BaseSnakeClient: WebSocketListener() {
    var socket: WebSocket? = null
    val LOG by lazyLogger()


    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        LOG.info("Connected to server")
        this.socket = webSocket
        sendHeartbeat()
    }

    private fun sendHeartbeat() {
        launch {
            delay(30, TimeUnit.SECONDS)
            LOG.info("Sending heartbeat")
            socket?.send("Heartbeat")
        }
    }

}