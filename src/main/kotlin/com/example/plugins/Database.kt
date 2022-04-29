package com.example.plugins

import com.example.favorites.FavoriteTable
import com.example.property
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    Database.connect(url = property("db.url"), driver = property("db.driver"))
    transaction {
        SchemaUtils.create(FavoriteTable)
    }
}
