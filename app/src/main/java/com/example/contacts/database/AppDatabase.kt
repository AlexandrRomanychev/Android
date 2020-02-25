package com.example.contacts.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.contacts.database.dao.ContactDao
import com.example.contacts.database.dao.LogUserDao
import com.example.contacts.database.dao.UserDao
import com.example.contacts.database.entity.Contact
import com.example.contacts.database.entity.LogUser
import com.example.contacts.database.entity.User

@Database(entities = [User::class, Contact::class, LogUser::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao?
    abstract fun contactDao(): ContactDao?
    abstract fun logUserDao(): LogUserDao?
}