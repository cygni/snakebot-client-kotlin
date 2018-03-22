package se.cygni.snake

import se.cygni.snake.client.BaseSnakeClient
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.logging.Logger


fun main(args: Array<String>) {

    val LOG = Logger.getLogger("Main")

    LOG.info("Main started")
    val client = BaseSnakeClient()


    val okHttpClient = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()

    val request = Request.Builder()
            .url("ws://localhost:8080/training")
            .build()

    LOG.info("Connecting...")
    val socket = okHttpClient.newWebSocket(request, client)

    LOG.info("Exiting")


}