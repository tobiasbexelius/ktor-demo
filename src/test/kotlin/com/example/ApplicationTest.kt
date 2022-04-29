package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.favorites.Favorite
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun `Can add and remove favorites`() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val user1Token = JWT.create()
            .withAudience("www.mycompany.se")
            .withIssuer("authservice.mycompany.se")
            .withClaim("Id", "user1")
            .sign(Algorithm.HMAC256("secret"))

        client.get("/favorites") { bearerAuth(user1Token) }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(emptyList(), body<List<Favorite>>())
        }
    }
}