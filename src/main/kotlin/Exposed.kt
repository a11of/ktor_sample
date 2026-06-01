package com.cyh

import io.ktor.server.application.Application
import org.jetbrains.exposed.v1.jdbc.Database

suspend fun Application.configureExposed() {
    Database.connect(
        url = "jdbc:mysql://192.168.5.29:3306/demo",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "123456"
    )
}