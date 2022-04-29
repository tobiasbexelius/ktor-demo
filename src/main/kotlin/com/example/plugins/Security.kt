package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.property
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.auth.principal

fun Application.configureSecurity() {
    install(Authentication) {
        jwt {
            val secret = this@configureSecurity.property("jwt.secret")
            val issuer = this@configureSecurity.property("jwt.issuer")
            val audience = this@configureSecurity.property("jwt.audience")
            realm = this@configureSecurity.property("jwt.realm")

            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(audience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}

val ApplicationCall.userId
    get() = principal<JWTPrincipal>()?.getClaim("Id", String::class)
        ?: throw AuthenticationException("No authenticated user")
