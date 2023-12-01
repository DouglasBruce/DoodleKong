package com.douglasbruce.data

import io.ktor.websocket.*

data class Player(
    val username: String,
    val socket: WebSocketSession,
    val clientId: String,
    var isDrawing: Boolean = false,
    var score: Int = 0,
    var rank: Int = 0,
)
