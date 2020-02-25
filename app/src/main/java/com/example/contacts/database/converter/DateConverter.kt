package com.example.contacts.database.converter

import androidx.room.TypeConverter
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateConverter {
    @JvmStatic
    @TypeConverter
    fun dateToTimestamp(date: String?): Long? {
        return if (date == null) {
            null
        } else {
            try {
                val localDate = SimpleDateFormat("dd.mm.yyyy").parse(date)
                localDate!!.time
            } catch (e: ParseException) {
                null
            }
        }
    }

    @JvmStatic
    @TypeConverter
    fun dateToString(date: Long?): String? {
        return if (date == null) null else {
            val localDate = Date(date)
            val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy")
            dateFormat.format(localDate)
        }
    }
}