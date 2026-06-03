package com.cyh

import io.ktor.server.application.Application
import io.ktor.server.plugins.di.annotations.Property
import org.jetbrains.exposed.v1.jdbc.Database

suspend fun Application.configureExposed(
    @Property("database.connectionUrl") connectionUrl: String,
    @Property("database.user") user: String,
    @Property("database.password") password: String,
    @Property("database.driver") driverClass: String = "org.postgresql.Driver"
) {
    Database.connect(
        url = connectionUrl,
        driver = driverClass,
        user = user,
        password = password
    )
}