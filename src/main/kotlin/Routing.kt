package com.cyh

import com.cyh.model.Priority
import com.cyh.model.Task
import com.cyh.model.User
import com.cyh.repository.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.http.content.resource
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.*
import io.ktor.server.routing.*

suspend fun Application.configureRouting() {
    val repository = dependencies.resolve<UserRepository>()

    routing {
        route("/user") {
            get("/all") {
                val all = repository.all()
                call.respond(all)
            }
            get("/add") {
                val name = call.request.queryParameters["name"]
                val email = call.request.queryParameters["email"]
                val password = call.request.queryParameters["password"]
                val phone = call.request.queryParameters["phone"]
                if (name != null && email != null && password != null && phone != null) {
                    val user = User(0, name, email, password, phone)
                    repository.add(user)
                    call.respond(user)
                    return@get
                }
                call.respond(HttpStatusCode.BadRequest)
            }
            get("/update") {
                val id = call.request.queryParameters["id"]
                val name = call.request.queryParameters["name"]
                val email = call.request.queryParameters["email"]
                val password = call.request.queryParameters["password"]
                val phone = call.request.queryParameters["phone"]
                if (id != null && name != null && email != null && password != null && phone != null) {
                    val user = User(id.toInt(), name, email, password, phone)
                    repository.update(user)
                    call.respond(user)
                    return@get
                }
                call.respond(HttpStatusCode.BadRequest)
            }
            get("/delete/{id}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                repository.removeById(id.toInt())
                call.respond(HttpStatusCode.OK)
            }
            get("/get/{id}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val user = repository.getById(id.toInt())
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(user)
            }
        }
    }
}