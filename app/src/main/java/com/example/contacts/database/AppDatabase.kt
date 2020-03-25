package com.example.contacts.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.contacts.database.dao.ContactDao
import com.example.contacts.database.entity.Contact

@Database(entities = [Contact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao?
}