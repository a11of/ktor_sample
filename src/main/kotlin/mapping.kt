package com.cyh

import com.cyh.model.Priority
import com.cyh.model.Task
import com.cyh.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.inTopLevelSuspendTransaction

object TaskTable : IntIdTable("task") {
    val name = varchar("name", 50)
    val description = varchar("description", 50)
    val priority = varchar("priority", 50)
}

class TaskDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TaskDAO>(TaskTable)
    var name by TaskTable.name
    var description by TaskTable.description
    var priority by TaskTable.priority
}
object UserTable : IntIdTable("users") {
    val name = varchar("name", 50)
    val email = varchar("email", 50)
    val hashPassword = varchar("hash_Password", 50)
    val phone = varchar("phone", 50)
}
class UserDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(UserTable)
    var name by UserTable.name
    var email by UserTable.email
    var hashPassword by UserTable.hashPassword
    var phone by UserTable.phone
}
suspend fun <T> withTransaction(block: suspend JdbcTransaction.() -> T): T = withContext(Dispatchers.IO) {
    inTopLevelSuspendTransaction { block() }
}

fun daoToModel(dao: TaskDAO) = Task(
    dao.name,
    dao.description,
    Priority.valueOf(dao.priority)
)
fun daoToModel(dao: UserDAO) = User(
    dao.id.value,
    dao.name,
    dao.hashPassword,
    dao.email,
    dao.phone,
)
