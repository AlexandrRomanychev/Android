package com.example.contacts.validation

import android.content.Context
import android.widget.Toast
import com.example.contacts.Profile
import com.example.contacts.async.AsyncUserAction
import com.example.contacts.database.AppDatabase
import com.example.contacts.database.DataBaseComands
import com.example.contacts.database.entity.User
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

object Validation {
    private val DATE_FORMAT: DateFormat = SimpleDateFormat("dd.MM.yyyy")
    @JvmStatic
    fun validateLoginPage(activity: Context?, login: String,
                          password: String, status: DataBaseComands?, db: AppDatabase?) {
        if (validateString(login)) Toast.makeText(activity, "Некорректный логин", Toast.LENGTH_SHORT).show() else if (validateString(password)) Toast.makeText(activity, "Некорректный пароль", Toast.LENGTH_SHORT).show() else {
            AsyncUserAction(activity!!, db!!,
                    User(login, password),
                    Profile::class.java, status!!).execute()
        }
    }

    @JvmStatic
    fun validateContactPage(activity: Context?, name: String, date: String,
                            phone: String): Boolean {
        if (validateString(name)) {
            Toast.makeText(activity, "Поле ФИО не заполнено", Toast.LENGTH_SHORT).show()
            return false
        }
        if (validateString(phone)) {
            Toast.makeText(activity, "Поле Телефон не заполнено", Toast.LENGTH_SHORT).show()
            return false
        }
        if (validateString(date)) {
            Toast.makeText(activity, "Поле Дата не заполнено", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!validateDateFormat(date)) {
            Toast.makeText(activity, "Дата имеет неверный формат", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun validateDateFormat(date: String): Boolean {
        return try {
            DATE_FORMAT.format(DATE_FORMAT.parse(date)) == date
        } catch (ex: ParseException) {
            false
        }
    }

    private fun validateString(item: String): Boolean {
        return item.trim { it <= ' ' } == ""
    }
}