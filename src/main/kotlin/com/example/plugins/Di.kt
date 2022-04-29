package com.example.plugins

import com.example.cocktaildb.CocktailDbClient
import com.example.favorites.FavoritesRepository
import com.example.favorites.FavoritesService
import com.example.koin.Koin
import com.example.property
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.logger.SLF4JLogger

fun Application.configureDependencyInjection() {
    val appModule = module {
        singleOf(::FavoritesService)
        singleOf(::FavoritesRepository)
        single { CocktailDbClient(httpClient = get(), cocktailDbUrl = property("cocktaildb.url")) }
        single { buildHttpClient() }
    }

    install(Koin) {
        SLF4JLogger()
        modules.add(appModule)
    }
}

fun buildHttpClient(): HttpClient =
    HttpClient(CIO) {
        install(Logging) {
            level = LogLevel.INFO
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }
