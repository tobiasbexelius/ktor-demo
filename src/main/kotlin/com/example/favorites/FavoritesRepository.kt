package com.example.favorites

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class FavoritesRepository {
    suspend fun getFavorites(userId: String): List<FavoriteEntity> = newSuspendedTransaction {
        FavoriteEntity.findByUserId(userId).toList()
    }

    suspend fun addFavorite(userId: String, drinkId: Long): Unit = newSuspendedTransaction {
        FavoriteEntity.add(userId, drinkId)
    }

    suspend fun removeFavorite(userId: String, drinkId: Long): Unit = newSuspendedTransaction {
        FavoriteEntity.remove(userId, drinkId)
    }
}

object FavoriteTable : LongIdTable() {
    val userId = varchar("userId", 256)
    val drinkId = long("drinkId")

    init {
        uniqueIndex("UniqueUserIdAndDrinkId", userId, drinkId)
    }
}

class FavoriteEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<FavoriteEntity>(FavoriteTable) {
        fun findByUserId(userId: String): Iterable<FavoriteEntity> =
            FavoriteEntity.find { FavoriteTable.userId eq userId }

        fun add(userId: String, drinkId: Long) {
            val existingFavoriteCount =
                FavoriteEntity.count(FavoriteTable.userId eq userId and (FavoriteTable.drinkId eq drinkId))

            if (existingFavoriteCount > 0) return

            FavoriteEntity.new {
                this.userId = userId
                this.drinkId = drinkId
            }
        }

        fun remove(userId: String, drinkId: Long) {
            FavoriteTable.deleteWhere { (FavoriteTable.userId eq userId) and (FavoriteTable.drinkId eq drinkId) }
        }
    }

    var userId by FavoriteTable.userId
    var drinkId by FavoriteTable.drinkId
}
