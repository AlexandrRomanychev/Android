package com.example.contacts.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.contacts.database.entity.User

@Dao
interface UserDao {
    @get:Query("SELECT * FROM user")
    val all: List<User?>?

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray?): List<User?>?

    @Query("SELECT COUNT(*) FROM user WHERE login = :login AND password = :password")
    fun findCountUsers(login: String?, password: String?): Int?

    @Query("SELECT COUNT(*) FROM user WHERE login = :login")
    fun findUserByLogin(login: String?): Int?

    @Insert
    fun insertAll(vararg users: User?)

    @Delete
    fun delete(user: User?)
}