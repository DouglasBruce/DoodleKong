package com.douglasbruce.routes

import com.douglasbruce.data.Player
import com.douglasbruce.data.Room
import com.douglasbruce.data.models.*
import com.douglasbruce.gson
import com.douglasbruce.server
import com.douglasbruce.session.DrawingSession
import com.douglasbruce.utils.Constants.TYPE_ANNOUNCEMENT
import com.douglasbruce.utils.Constants.TYPE_CHAT_MESSAGE
import com.douglasbruce.utils.Constants.TYPE_CHOSEN_WORD
import com.douglasbruce.utils.Constants.TYPE_DRAW_DATA
import com.douglasbruce.utils.Constants.TYPE_GAME_STATE
import com.douglasbruce.utils.Constants.TYPE_JOIN_ROOM_HANDSHAKE
import com.douglasbruce.utils.Constants.TYPE_PHASE_CHANGE
import com.google.gson.JsonParser
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.gameWebSocketRoute() {
    route("/ws/draw") {
        standardWebSocket { socket, clientId, message, payload ->
            when (payload) {
                is DrawData -> {
                    val room = server.rooms[payload.roomName] ?: return@standardWebSocket
                    if (room.phase == Room.Phase.GAME_RUNNING) {
                        room.broadcastToAllExcept(message, clientId)
                    }
                }

                is ChatMessage -> {

                }

                is JoinRoomHandshake -> {
                    val room = server.rooms[payload.roomName]
                    if (room == null) {
                        val gameError = GameError(GameError.ERROR_ROOM_NOT_FOUND)
                        socket.send(Frame.Text(gson.toJson(gameError)))
                        return@standardWebSocket
                    }

                    val player = Player(payload.username, socket, payload.clientId)
                    server.playerJoined(player)

                    if (!room.containsPlayer(player.username)) {
                        room.addPlayer(player.clientId, player.username, socket)
                    }
                }

                is ChosenWord -> {
                    val room = server.rooms[payload.roomName] ?: return@standardWebSocket
                    room.setWordAndSwitchToGameRunning(payload.chosenWord)
                }
            }
        }
    }
}

fun Route.standardWebSocket(
    handleFrame: suspend (
        socket: DefaultWebSocketServerSession,
        clientId: String,
        message: String,
        payload: BaseModel,
    ) -> Unit
) {
    webSocket {
        val session = call.sessions.get<DrawingSession>()
        if (session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session."))
            return@webSocket
        }

        try {
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val message = frame.readText()
                    val jsonObject = JsonParser.parseString(message).asJsonObject
                    val type = when (jsonObject.get("type").asString) {
                        TYPE_CHAT_MESSAGE -> ChatMessage::class.java
                        TYPE_DRAW_DATA -> DrawData::class.java
                        TYPE_ANNOUNCEMENT -> Announcement::class.java
                        TYPE_JOIN_ROOM_HANDSHAKE -> JoinRoomHandshake::class.java
                        TYPE_PHASE_CHANGE -> PhaseChange::class.java
                        TYPE_CHOSEN_WORD -> ChosenWord::class.java
                        TYPE_GAME_STATE -> GameState::class.java
                        else -> BaseModel::class.java
                    }
                    val payload = gson.fromJson(message, type)
                    handleFrame(this, session.clientId, message, payload)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // Handle disconnects
        }
    }
}