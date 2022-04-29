package com.example.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.path
import io.ktor.server.response.respond

fun Application.configureExceptionHandling() {
    install(StatusPages) {
        exception<AuthenticationException> { call, cause ->
            this@configureExceptionHandling.log.info(call.request.path(), cause)
            call.respond(HttpStatusCode.Unauthorized)
        }
    }
}

class AuthenticationException(message: String) : RuntimeException(message)
