package com.example.contacts.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.contacts.database.converter.DateConverter

@Entity
class Contact {
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var uid = 0
    @JvmField
    @ColumnInfo(name = "name")
    var name: String
    @JvmField
    @ColumnInfo(name = "phone")
    var phone: String
    @JvmField
    @ColumnInfo(name = "date")
    var date: Long
    @JvmField
    @ColumnInfo(name = "photo")
    var photo: String
    @JvmField
    @ColumnInfo(name = "login")
    var login: String

    constructor(name: String, date: Long, phone: String,
                photo: String, login: String) {
        this.name = name
        this.date = date
        this.phone = phone
        this.photo = photo
        this.login = login
    }

    constructor(name: String, date: Long, phone: String,
                photo: String, id: Int, login: String) {
        this.name = name
        this.date = date
        this.phone = phone
        this.photo = photo
        uid = id
        this.login = login
    }
}