package com.douglasbruce.plugins

import com.douglasbruce.routes.createRoomRoute
import com.douglasbruce.routes.getRoomsRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(Routing) {
        createRoomRoute()
        getRoomsRoute()
    }
}
