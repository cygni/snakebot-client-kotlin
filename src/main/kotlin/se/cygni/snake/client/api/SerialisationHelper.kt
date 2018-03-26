package se.cygni.snake.client.api

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.BooleanNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import se.cygni.snake.client.api.model.*
import java.io.IOException

val mapper = jacksonObjectMapper()

fun getCustomMapper(): ObjectMapper {
    val module: SimpleModule = SimpleModule("custom", Version.unknownVersion())
    module.addDeserializer(GameMessage::class.java, Deserializer())
    module.addSerializer(GameMessage::class.java, Serializer())
    val jacksonObjectMapper = jacksonObjectMapper()
    jacksonObjectMapper.registerModule(module)
    return jacksonObjectMapper
}

class Deserializer: JsonDeserializer<GameMessage>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): GameMessage {
        val node: JsonNode = p.getCodec().readTree(p)
        val type: String = node.get("type").asText()
        val messageType = GameMessageType.fromTypeString(type)
        return mapper.treeToValue(node, messageType.klass.java)
    }
}

class Serializer: JsonSerializer<GameMessage>() {
    override fun serialize(value: GameMessage, gen: JsonGenerator, serializers: SerializerProvider) {
        val gameMessageType = GameMessageType.fromClass(value::class)
        gen.writeStartObject()
        gen.writeObjectField("type", gameMessageType.type)

        val node: JsonNode = mapper.valueToTree(value)
        node.fields().forEach {
            gen.writeObjectField(it.key, it.value)
        }
        gen.writeEndObject()
    }
}


fun GameMessage.encode(): String = getCustomMapper().writeValueAsString(this)
fun String.decode(): GameMessage = getCustomMapper().readValue(this)


// TODO Cleanup of this below in Kotlin-way
class TileContentDeserializer : JsonDeserializer<TileContent>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): TileContent {
        val node = jp.codec.readTree<JsonNode>(jp)

        val content = node.get("content").asText()

        var name = ""
        if (node.has("name"))
            name = node.get("name").asText()

        var playerId = ""
        if (node.has("playerId"))
            playerId = node.get("playerId").asText()

        var order = -1
        if (node.has("order"))
            order = (node.get("order") as IntNode).numberValue() as Int

        var tail = false
        if (node.has("tail"))
            tail = (node.get("tail") as BooleanNode).booleanValue()

        return when (content) {
            MapObstacle.CONTENT -> MapObstacle()
            MapFood.CONTENT -> MapFood()
            MapSnakeHead.CONTENT -> MapSnakeHead(name, playerId)
            MapSnakeBody.CONTENT -> MapSnakeBody(tail, playerId, order)
            else -> MapEmpty()
        }
    }
}
