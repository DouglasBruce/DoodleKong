package com.douglasbruce

import com.douglasbruce.plugins.*
import com.google.gson.Gson
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

val server = DrawingServer()
val gson = Gson()

fun Application.module() {
    configureSession()
    configureSerialization()
    configureSockets()
    configureMonitoring()
    configureRouting()
}
