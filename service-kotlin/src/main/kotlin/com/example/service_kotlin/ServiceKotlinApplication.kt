package com.example.service_kotlin

import io.r2dbc.spi.Readable
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.beans.factory.BeanRegistrarDsl
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.data.annotation.Id
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.security.config.Customizer
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import org.springframework.web.reactive.function.server.coRouter
import reactor.core.publisher.Mono

@Import(ApplicationConfiguration::class)
@SpringBootApplication
class ServiceKotlinApplication

fun main(args: Array<String>) {
    runApplication<ServiceKotlinApplication>(*args)
}

class SqlReactiveUserDetailsService(private val db: DatabaseClient) : ReactiveUserDetailsService {

    override fun findByUsername(username: String): Mono<UserDetails> {
        return db
            .sql(" select * from users where username = :name ")
            .bind("name", username)
            .map<UserDetails> { readable: Readable ->
                User(
                    (readable.get("username") as String),
                    readable.get("password") as String,
                    listOf(SimpleGrantedAuthority("ROLE_USER"))
                )
            }
            .first()
    }

}

class ApplicationConfiguration : BeanRegistrarDsl({

    registerBean<SqlReactiveUserDetailsService>()

    registerBean<Customizer<ServerHttpSecurity>> {
        Customizer { http ->
            http.authorizeExchange {
                it.pathMatchers("/dogs").permitAll()
            }
        }
    }

    registerBean {
        val repo = bean<DogRepository>()
        coRouter {
            GET("/me") {
                val map = it.principal().map { p -> mapOf("name" to p.name) }.awaitSingle()
                ServerResponse.ok().bodyValueAndAwait(map)
            }
            GET("/dogs") {
                val name = it.queryParam("name").orElse(null)
                if (name != null) {
                    val dog = repo.findByName(name).awaitSingleOrNull()
                        ?: return@GET ServerResponse.notFound().buildAndAwait()
                    ServerResponse.ok().bodyValueAndAwait(dog)
                } else {
                    ServerResponse.ok().bodyAndAwait(repo.findAll().asFlow())
                }
            }
        }
    }
})

interface DogRepository : ReactiveCrudRepository<Dog, Int> {
    fun findByName(name: String): Mono<Dog>
}

data class Dog(val name: String, @Id val id: Int)

