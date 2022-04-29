package com.example.plugins

import com.example.favorites.favoritesController
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.resources.Resources
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    install(Resources)
    install(DefaultHeaders)
    routing {
        authenticate {
            favoritesController()
        }
    }
}
