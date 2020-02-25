package com.example.contacts.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.contacts.database.entity.LogUser

@Dao
interface LogUserDao {
    @get:Query("SELECT * FROM loguser LIMIT 1")
    val logUser: LogUser?

    @Insert
    fun insertAll(users: LogUser?)

    @Query("DELETE FROM loguser WHERE login = :login")
    fun deleteAll(login: String?)
}