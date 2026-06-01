package com.cyh.repository

import com.cyh.UserDAO
import com.cyh.UserTable
import com.cyh.UserTable.hashPassword
import com.cyh.daoToModel
import com.cyh.model.User
import com.cyh.withTransaction
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere

interface UserRepository {
    suspend fun all(): List<User>
    suspend fun getById(id: Int): User?
    suspend fun removeById(id: Int): Boolean
    suspend fun add(user: User): UserDAO
    suspend fun update(user: User): User?
}
class MysqlUserRepository : UserRepository {
    override suspend fun all(): List<User>  = withTransaction {
        UserDAO.all().map(::daoToModel)
    }

    override suspend fun getById(id: Int): User? = withTransaction {
        UserDAO.find {
            UserTable.id.eq(id)
        }.limit(1).map(::daoToModel).firstOrNull()
    }

    override suspend fun removeById(id: Int)  = withTransaction {
        val deleteWhere = UserTable.deleteWhere { UserTable.id eq id }
        deleteWhere == 1
    }

    override suspend fun add(user: User) = withTransaction {
        UserDAO.new {
            name = user.name
            email = user.email
            phone = user.phone
            hashPassword = user.hashPassword
        }
    }

    override suspend fun update(user: User) = withTransaction {
        val userDAO = UserDAO.findByIdAndUpdate(user.id) {
            it.name = user.name
            it.email = user.email
            it.phone = user.phone
            it.hashPassword = user.hashPassword
        }
        userDAO?.let {
            daoToModel(it)
        }
    }


}