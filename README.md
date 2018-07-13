# snakebot-client-kotlin
This a Snake Client written in Kotlin

## Requirements

* Java JDK >= 8
* Gradle
* Snake Server (local or remote)


## Installation

A. Clone the repository: `git clone https://github.com/cygni/snakebot-client-kotlin.git`.

B. Open: `<repo>/`

C. Execute: `./gradlew build`


## Usage

To clean and build:
```
> ./gradlew clean build
```

To run your client:
```
> ./gradlew run
```

## Implementation

There is one class you should focus on for your implementation have `ExampleSnakePlayer.kt` The method to start in looks like this:

```kotlin

override fun onMapUpdate(mapUpdateEvent: MapUpdateEvent) {
    // Input your implementation here, below is a dummy example implementation that
    // goes into a random valid direction at every step


    // MapUtil contains lot's of useful methods for querying the map!
    val mapUtil = MapUtil(mapUpdateEvent.map, playerId)

    // Let's see in which directions I can move
    val directions = SnakeDirection.values().filter(mapUtil::canIMoveInDirection)

    val r = Random()
    var chosenDirection = SnakeDirection.DOWN
    // Choose a random direction
    if (!directions.isEmpty()) {
        chosenDirection = directions[r.nextInt(directions.size)]
    }

    // Register action here!
    registerMove(mapUpdateEvent.gameTick, chosenDirection)
}

```

For every MapUpdateEvent received your are expected to reply with a SnakeDirection (UP, DOWN, LEFT or RIGHT). 