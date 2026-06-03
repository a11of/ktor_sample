package com.cyh

import com.cyh.repository.FakeTaskRepository
import com.cyh.repository.MysqlUserRepository
import com.cyh.repository.RedisUserRepository
import com.cyh.repository.TaskRepository
import com.cyh.repository.UserRepository
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode

import io.ktor.server.application.*
import io.ktor.server.http.HttpRequestLifecycle
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.plugins.methodoverride.XHttpMethodOverride
import io.ktor.server.resources.Resources
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.util.getValue
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.serialization.Serializable

data class Person<T>(val name: T, val age: Int)
fun Application.configureDependencies() {
    dependencies {
        provide<TaskRepository> {
            FakeTaskRepository()
        }
        provide<UserRepository>("mysql"){MysqlUserRepository()}
        provide<UserRepository>("redis"){ RedisUserRepository() }
    }
}
fun Application.configureSecurity() {
    val port = environment.config.propertyOrNull("ktor.deployment.port")?.getString() ?: "8080"
    log.info("Starting application with port $port")
    dependencies {
        provide<List<String>> { listOf("one", "two", "three", "four", "five", "six", "seven", "eight") }
        provide<Person<String>>{Person("abc",1)}
        provide<Person<Int>>{Person(123,1)}
    }



}

/**
 * 通过resource可以映射路由信息，在获取参数时比较方便
 */
fun Application.module() {
    install(Resources)
    install(HttpRequestLifecycle) {
        cancelCallOnClose = true
    }
    routing {
        get<Articles> { article ->
            // Get all articles ...
            call.respondText("List of articles sorted starting from ${article.sort}")
        }
        get<Articles.New> {
            // Show a page with fields for creating a new article ...
            call.respondText("Create a new article")
        }
        post<Articles> {
            // Save an article ...
            call.respondText("An article is saved", status = HttpStatusCode.Created)
        }
        get<Articles.Id> { article ->
            // Show an article with id ${article.id} ...
            call.respondText("An article with id ${article.id}", status = HttpStatusCode.OK)
        }
        get<Articles.Id.Edit> { article ->
            // Show a page with fields for editing an article ...
            call.respondText("Edit an article with id ${article.parent.id}", status = HttpStatusCode.OK)
        }
        put<Articles.Id> { article ->
            // Update an article ...
            call.respondText("An article with id ${article.id} updated", status = HttpStatusCode.OK)
        }
        delete<Articles.Id> { article ->
            // Delete an article ...
            call.respondText("An article with id ${article.id} deleted", status = HttpStatusCode.OK)
        }
        get("/long-process") {
            try {
                while (isActive) {
                    delay(10_000)
                    log.info("Very important work.")
                }
                call.respond("Completed")
            } catch (e: CancellationException) {
                log.info("Cleaning up resources.")
            }
        }
    }
    module1()
    module2()
}
@Serializable
data class Customer(val id: Int, val firstName: String, val lastName: String)
/**
 *
 */
fun Application.module1() {
    // This will work due to type parameter covariance support
    val stringList: List<CharSequence> by dependencies
    val person: Person<String> by dependencies
    val customerStorage = mutableListOf<Customer>()
    customerStorage.addAll(
        arrayOf(
            Customer(1, "Jane", "Smith"),
            Customer(2, "John", "Smith"),
            Customer(3, "Jet", "Brains")
        )
    )
//    install(XHttpMethodOverride)
    routing {
        get("/customer/{id}/{name}") {
            val id: Int by call.parameters
            val name: String by call.parameters
            val customer: Customer = customerStorage.find { it.id == id }!!
            val copy = customer.copy(firstName = name, lastName = name)
            call.respond(copy)
        }

        delete("/customer/{id}") {
            val id: Int by call.parameters
            customerStorage.removeIf { it.id == id }
            call.respondText("Customer is removed", status = HttpStatusCode.NoContent)
        }
    }
}
fun Application.module2() {
    // This will also work
    val stringCollection: Collection<CharSequence> by dependencies
    val person: Person<Int> by dependencies
    routing {
        get("/module2") {
            call.respondText {"${ stringCollection} \n $person" }
        }
    }
}


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

