package com.example

import io.ktor.server.application.*
import com.example.plugins.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureSecurity()
    configureRouting()
    configureMonitoring()
    configureSerialization()
    configureDependencyInjection()
    configureExceptionHandling()
    configureDatabase()
}

fun Application.property(key: String): String = environment.config.property(key).getString()
