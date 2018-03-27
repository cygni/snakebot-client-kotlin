package se.cygni.snake.client.api.model

class PlayerRank(
        val playerName: String,
        val playerId: String,
        val rank: Int,
        val points: Int,
        val alive: Boolean) {

    override fun toString(): String {
        return "$rank.\t$points pts\t$playerName (${if (alive) "alive" else "dead"})"
    }
}
