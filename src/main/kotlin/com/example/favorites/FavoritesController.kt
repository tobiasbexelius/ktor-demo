package com.example.favorites

import com.example.koin.inject
import com.example.plugins.userId
import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.application.call
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.resources.put
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import kotlinx.serialization.Serializable
import org.koin.java.KoinJavaComponent.inject

@Serializable
@Resource("/favorites")
private class FavoritesResource {

    @Serializable
    @Resource("{drinkIdRaw}")
    class DrinkId(
        @Suppress("unused") val parent: FavoritesResource,
        @Suppress("CanBeParameter") val drinkIdRaw: String
    ) {
        val drinkId: Long = drinkIdRaw.toLongOrNull() ?: throw BadRequestException("Invalid drinkId: $drinkIdRaw")
    }
}

fun Route.favoritesController(): Route = apply {
    val service: FavoritesService by inject()

    get<FavoritesResource> {
        call.respond(service.getFavorites(call.userId))
    }

    put<FavoritesResource.DrinkId> { request ->
        call.respond(HttpStatusCode.NoContent, service.addFavorite(call.userId, request.drinkId))
    }

    delete<FavoritesResource.DrinkId> { request ->
        call.respond(HttpStatusCode.NoContent, service.removeFavorite(call.userId, request.drinkId))
    }
}
