package com.example.plugins

import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callloging.CallLogging
import org.slf4j.event.Level
import java.util.UUID

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
    }

    install(CallId) {
        header(HttpHeaders.XRequestId)
        generate { UUID.randomUUID().toString() }
        verify { callId: String ->
            callId.isNotEmpty()
        }
    }
}
