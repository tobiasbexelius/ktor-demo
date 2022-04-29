package com.example.cocktaildb

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.Serializable

class CocktailDbClient(
    private val httpClient: HttpClient,
    private val cocktailDbUrl: String,
) {

    suspend fun getDrinkName(drinkId: Long): String {
        val response: HttpResponse = httpClient.get("$cocktailDbUrl/lookup.php") {
            parameter("i", drinkId)
        }
        return response.body<LookupResult>().drinks!!.first().strDrink
    }

    suspend fun isValidDrinkId(drinkId: Long): Boolean {
        val lookupResult = httpClient.get("$cocktailDbUrl/lookup.php") {
            parameter("i", drinkId)
        }
        return !lookupResult.body<LookupResult>().drinks.isNullOrEmpty()
    }
}

@Serializable
private data class LookupResult(val drinks: List<Drink>?)

@Serializable
private data class Drink(val strDrink: String)
