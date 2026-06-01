package com.cyh

import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.formUrlEncode
import io.ktor.server.testing.testApplication
import kotlin.test.*

class ServerTest {

    @Test
    fun `test root endpoint`() = testApplication {
        // loads default configuration
        configure()
        // verify server root returns 200
        assertEquals(HttpStatusCode.OK, client.get("/").status)
    }
    @Test
    fun tasksCanBeFoundByPriority() = testApplication {
        configure()
        val response = client.get("/priority/admin")
        val body = response.bodyAsText()

        assertEquals(HttpStatusCode.OK, response.status)
        assertContains(body, "Hello,admin!")
    }
    @Test
    fun newTasksCanBeAdded() = testApplication {
        configure()

        val response1 = client.post("/tasks") {
            header(
                HttpHeaders.ContentType,
                ContentType.Application.FormUrlEncoded.toString()
            )
            setBody(
                listOf(
                    "name" to "swimming",
                    "description" to "Go to the beach",
                    "priority" to "Low"
                ).formUrlEncode()
            )
        }
        assertEquals(HttpStatusCode.OK, response1.status)

        val response2 = client.post("/tasks")
        assertEquals(HttpStatusCode.UnsupportedMediaType, response2.status)
        val body = response1.bodyAsText()

        assertContains(body, "swimming")
        assertContains(body, "Go to the beach")
    }
}
