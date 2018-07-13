package se.cygni.snake

import org.slf4j.LoggerFactory
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
