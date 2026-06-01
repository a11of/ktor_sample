package com.cyh

import com.cyh.repository.FakeTaskRepository
import com.cyh.repository.MysqlUserRepository
import com.cyh.repository.TaskRepository
import com.cyh.repository.UserRepository
import io.ktor.server.engine.*
import io.ktor.server.application.*
import io.ktor.server.plugins.di.dependencies
fun Application.configureDependencies() {
    dependencies {
        provide<TaskRepository> {
            FakeTaskRepository()
        }
        provide<UserRepository>{
            MysqlUserRepository()
        }
    }
}
fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

