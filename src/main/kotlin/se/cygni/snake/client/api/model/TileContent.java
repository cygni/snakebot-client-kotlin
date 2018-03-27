package se.cygni.snake.client.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.cygni.snake.client.api.TileDeserializer;

@JsonDeserialize(using = TileDeserializer.class)
public interface TileContent {
    String getContent();
    String toDisplay();
}
