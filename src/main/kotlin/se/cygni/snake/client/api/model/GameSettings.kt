package se.cygni.snake.client.api.model

data class GameSettings(
        // Maximum noof players in this game
        val maxNoofPlayers: Int = 5,
        // The starting length of a snake
        val startSnakeLength: Int = 1,
        // The time clients have to respond with a new move
        val timeInMsPerTick: Int = 250,
        // Randomly place obstacles
        val obstaclesEnabled: Boolean = true,
        // Randomly place food
        val foodEnabled: Boolean = true,
        // If a snake manages to nibble on the tail
        // of another snake it will consume that tail part.
        // I.e. the nibbling snake will grow one and
        // victim will loose one.
        val headToTailConsumes: Boolean = true,
        // Only valid if headToTailConsumes is active.
        // When tailConsumeGrows is set to true the
        // consuming snake will grow when eating
        // another snake.
        val tailConsumeGrows: Boolean = false,
        // Likelihood (in percent) that a new food will be
        // added to the world
        val addFoodLikelihood: Int = 15,
        // Likelihood (in percent) that a
        // food will be removed from the world
        val removeFoodLikelihood: Int = 5,
        // Snake grow every N world ticks.
        // 0 for disabled
        val spontaneousGrowthEveryNWorldTick: Int = 3,
        // Indicates that this is a training game,

        // Bots will be added to fill up remaining players.
        val trainingGame: Boolean = false,
        // Points given per length unit the Snake has
        val pointsPerLength: Int = 1,
        // Points given per Food item consumed
        val pointsPerFood: Int = 2,
        // Points given per caused death (i.e. another
        // snake collides with yours)
        val pointsPerCausedDeath: Int = 5,
        // Points given when a snake nibbles the tail
        // of another snake
        val pointsPerNibble: Int = 10,
        // Number of rounds a tail is protected after nibble
        val noofRoundsTailProtectedAfterNibble: Int = 3,
        // The starting count for food
        val startFood: Int = 0,
        // The starting count for obstacles
        val startObstacles: Int = 5)
