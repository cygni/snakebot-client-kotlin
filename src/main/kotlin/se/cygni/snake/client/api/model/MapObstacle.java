package se.cygni.snake.client.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MapObstacle implements TileContent {

    public static final String CONTENT = "obstacle";

    @Override
    public String getContent() {
        return CONTENT;
    }

    @JsonIgnore
    public String toDisplay() {
        return "O";
    }
}
