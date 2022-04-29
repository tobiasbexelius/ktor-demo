package com.example.favorites

import com.example.cocktaildb.CocktailDbClient
import io.ktor.server.plugins.NotFoundException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.Serializable

class FavoritesService(
    private val favoritesRepository: FavoritesRepository,
    private val racingInfoClient: CocktailDbClient,
) {
    suspend fun getFavorites(userId: String): List<Favorite> =
        favoritesRepository.getFavorites(userId).map { entity ->
            coroutineScope {
                async {
                    val drinkName = racingInfoClient.getDrinkName(entity.drinkId)
                    Favorite(entity.drinkId, drinkName)
                }
            }
        }.awaitAll()

    suspend fun addFavorite(userId: String, drinkId: Long) {
        if (!racingInfoClient.isValidDrinkId(drinkId)) throw NotFoundException("Invalid drinkId: $drinkId")
        favoritesRepository.addFavorite(userId, drinkId)
    }

    suspend fun removeFavorite(userId: String, drinkId: Long): Unit =
        favoritesRepository.removeFavorite(userId, drinkId)
}

@Serializable
data class Favorite(val drinkId: Long, val drinkName: String)
