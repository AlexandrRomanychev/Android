package com.example.contacts.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.contacts.database.entity.Contact

@Dao
interface ContactDao {
    @Query("SELECT * FROM contact WHERE name LIKE :rule AND login = :login")
    fun getAll(rule: String?, login: String?): List<Contact>?

    @Query("SELECT * FROM contact WHERE login = :login AND name LIKE :rule ORDER BY name ASC")
    fun getSortedByNameUp(rule: String?, login: String?): List<Contact>?

    @Query("SELECT * FROM contact WHERE login = :login AND name LIKE :rule ORDER BY name DESC")
    fun getSortedByNameDown(rule: String?, login: String?): List<Contact>?

    @Query("SELECT * FROM contact WHERE login = :login AND name LIKE :rule ORDER BY date ASC")
    fun getSortedByDateUp(rule: String?, login: String?): List<Contact>?

    @Query("SELECT * FROM contact WHERE login = :login AND name LIKE :rule ORDER BY date DESC")
    fun getSortedByDateDown(rule: String?, login: String?): List<Contact>?

    @Query("UPDATE contact SET name = :name, date = :date, phone = :phone, photo = :photo WHERE uid IN (:contactIds) AND login = :login")
    fun updateContact(name: String?, date: Long, phone: String?, photo: String?, contactIds: IntArray?, login: String?)

    @Insert
    fun insertAll(vararg contacts: Contact?)

    @Delete
    fun delete(contact: Contact?)
}