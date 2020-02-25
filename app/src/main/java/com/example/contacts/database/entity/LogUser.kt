package com.example.contacts.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class LogUser(@field:ColumnInfo(name = "login") var login: String, @field:ColumnInfo(name = "password") var password: String) {
    @JvmField
    @PrimaryKey(autoGenerate = true)
    var uid = 0

}