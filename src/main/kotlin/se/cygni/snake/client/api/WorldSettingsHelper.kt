package se.cygni.snake.client.api

import se.cygni.snake.client.api.model.GameSettings


fun trainingWorld(): GameSettings {
    return GameSettings(
            maxNoofPlayers = 5,
            foodEnabled = true,
            obstaclesEnabled = true
    )
}

fun eightPlayerWorld(): GameSettings {
    return GameSettings(
            maxNoofPlayers = 8,
            foodEnabled = true,
            obstaclesEnabled = false)
}

fun twelvePlayerWorld(): GameSettings {
    return GameSettings(
            maxNoofPlayers = 12,
            foodEnabled = true,
            obstaclesEnabled = false)
}

fun defaultTournament(): GameSettings {
    return GameSettings(
            maxNoofPlayers = 15,
            foodEnabled = true,
            obstaclesEnabled = true)
}
