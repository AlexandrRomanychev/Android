package com.example.contacts.async

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.widget.Toast
import com.example.contacts.database.AppDatabase
import com.example.contacts.database.DataBaseComands
import com.example.contacts.database.entity.LogUser
import com.example.contacts.database.entity.User

class AsyncUserAction(private val activity: Context, private val db: AppDatabase, private val user: User?, private val nextPage: Class<*>, private val status: DataBaseComands) : AsyncTask<Void?, Void?, Int?>() {
    private var logUser: LogUser? = null
    override fun doInBackground(vararg params: Void?): Int? {
        when (status) {
            DataBaseComands.USER_ENTER -> {
                return db.userDao()!!.findCountUsers(user!!.login, user!!.password)
            }
            DataBaseComands.USER_CHECK_REGISTRY -> {
                return db.userDao()!!.findUserByLogin(user!!.login)
            }
            DataBaseComands.USER_REGISTRY -> {
                db.userDao()!!.insertAll(user)
            }
            DataBaseComands.USER_ADD -> {
                db.logUserDao()!!.insertAll(LogUser(user!!.login, user!!.password))
            }
            DataBaseComands.USER_GET_LOGINED -> {
                logUser = db.logUserDao()!!.logUser
            }
            DataBaseComands.USER_DELETE -> {
                db.logUserDao()!!.deleteAll(user!!.login)
            }
            else -> {
                return 0
            }
        }
        return 0
    }

    override fun onPostExecute(result: Int?) {
        when (status) {
            DataBaseComands.USER_ENTER -> {
                if (result!! > 0) {
                    Toast.makeText(activity, "Успешный вход", Toast.LENGTH_SHORT).show()
                    AsyncUserAction(activity, db, user, nextPage, DataBaseComands.USER_ADD).execute()
                } else {
                    Toast.makeText(activity, "Неверный логин/пароль", Toast.LENGTH_SHORT).show()
                }
            }
            DataBaseComands.USER_CHECK_REGISTRY -> {
                if (result!! > 0) {
                    Toast.makeText(activity, "Пользователь с таким логином уже существует!", Toast.LENGTH_SHORT).show()
                } else {
                    AsyncUserAction(activity, db, user, nextPage, DataBaseComands.USER_REGISTRY).execute()
                }
            }
            DataBaseComands.USER_REGISTRY -> {
                Toast.makeText(activity, "Успешная регистрация", Toast.LENGTH_SHORT).show()
                AsyncUserAction(activity, db, user, nextPage, DataBaseComands.USER_ADD).execute()
            }
            DataBaseComands.USER_GET_LOGINED -> {
                if (logUser != null) {
                    val intent = Intent(activity, nextPage)
                    intent.putExtra("login", logUser!!.login)
                    activity.startActivity(intent)
                }
            }
            DataBaseComands.USER_ADD -> {
                AsyncUserAction(activity, db, user, nextPage, DataBaseComands.USER_GET_LOGINED).execute()
            }
        }
    }

}