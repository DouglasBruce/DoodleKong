package com.douglasbruce.routes

import com.douglasbruce.data.Room
import com.douglasbruce.data.models.BasicApiResponse
import com.douglasbruce.data.models.CreateRoomRequest
import com.douglasbruce.server
import com.douglasbruce.utils.Constants.MAX_ROOM_SIZE
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createRoomRoute() {
    route("/api/createRoom") {
        post {
            val roomRequest = call.receiveNullable<CreateRoomRequest>()
            if (roomRequest == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            if (server.rooms[roomRequest.name] != null) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(false, "Room already exists."),
                )
                return@post
            }

            if (roomRequest.maxPlayers < 2) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(false, "The minimum room size is 2."),
                )
                return@post
            }

            if (roomRequest.maxPlayers > MAX_ROOM_SIZE) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(false, "The maximum room size is $MAX_ROOM_SIZE."),
                )
                return@post
            }

            val room = Room(
                roomRequest.name,
                roomRequest.maxPlayers
            )
            server.rooms[roomRequest.name] = room
            call.respond(HttpStatusCode.OK, BasicApiResponse(true))
        }
    }
}