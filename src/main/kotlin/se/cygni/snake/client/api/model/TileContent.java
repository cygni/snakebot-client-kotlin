package se.cygni.snake.client.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import se.cygni.snake.client.api.TileContentDeserializer;

@JsonDeserialize(using = TileContentDeserializer.class)
public interface TileContent {
    String getContent();
    String toDisplay();
}
