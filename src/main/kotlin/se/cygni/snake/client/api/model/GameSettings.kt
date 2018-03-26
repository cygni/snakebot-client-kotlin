package se.cygni.snake.client.api.model

data class GameSettings(
        // Maximum noof players in this game
        var maxNoofPlayers: Int = 5,
        // The starting length of a snake
        var startSnakeLength: Int = 1,
        // The time clients have to respond with a new move
        var timeInMsPerTick: Int = 250,
        // Randomly place obstacles
        var obstaclesEnabled: Boolean = true,
        // Randomly place food
        var foodEnabled: Boolean = true,
        // If a snake manages to nibble on the tail
        // of another snake it will consume that tail part.
        // I.e. the nibbling snake will grow one and
        // victim will loose one.
        var headToTailConsumes: Boolean = true,
        // Only valid if headToTailConsumes is active.
        // When tailConsumeGrows is set to true the
        // consuming snake will grow when eating
        // another snake.
        var tailConsumeGrows: Boolean = false,
        // Likelihood (in percent) that a new food will be
        // added to the world
        var addFoodLikelihood: Int = 15,
        // Likelihood (in percent) that a
        // food will be removed from the world
        var removeFoodLikelihood: Int = 5,
        // Snake grow every N world ticks.
        // 0 for disabled
        var spontaneousGrowthEveryNWorldTick: Int = 3,
        // Indicates that this is a training game,

        // Bots will be added to fill up remaining players.
        var trainingGame: Boolean = false,
        // Points given per length unit the Snake has
        var pointsPerLength: Int = 1,
        // Points given per Food item consumed
        var pointsPerFood: Int = 2,
        // Points given per caused death (i.e. another
        // snake collides with yours)
        var pointsPerCausedDeath: Int = 5,
        // Points given when a snake nibbles the tail
        // of another snake
        var pointsPerNibble: Int = 10,
        // Number of rounds a tail is protected after nibble
        var noofRoundsTailProtectedAfterNibble: Int = 3,
        // The starting count for food
        var startFood: Int = 0,
        // The starting count for obstacles
        var startObstacles: Int = 5)
