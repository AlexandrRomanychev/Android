package com.example.contacts

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.contacts.async.AsyncUserAction
import com.example.contacts.database.AppDatabase
import com.example.contacts.database.DataBaseComands
import com.example.contacts.validation.Validation.validateLoginPage

class MainActivity : AppCompatActivity() {
    private var login: EditText? = null
    private var password: EditText? = null
    private var db: AppDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = Room.databaseBuilder(this@MainActivity, AppDatabase::class.java, "contacts").build()
        login = findViewById(R.id.login)
        password = findViewById(R.id.password)
        val enter = findViewById<Button>(R.id.authorization)
        val registry = findViewById<Button>(R.id.registration)
        AsyncUserAction(this@MainActivity, db!!, null, Profile::class.java, DataBaseComands.USER_GET_LOGINED).execute()
        enter.setOnClickListener {
            validateLoginPage(this@MainActivity, login!!.text.toString(),
                    password!!.text.toString(), DataBaseComands.USER_ENTER, db)
        }
        registry.setOnClickListener {
            validateLoginPage(this@MainActivity, login!!.text.toString(),
                    password!!.text.toString(), DataBaseComands.USER_CHECK_REGISTRY, db)
        }
    }

    // сохранение состояния
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("loginSave", login!!.text.toString())
        outState.putString("passwordSave", password!!.text.toString())
        super.onSaveInstanceState(outState)
    }

    // получение сохраненного состояния
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        login!!.setText(savedInstanceState.getString("loginSave"))
        password!!.setText(savedInstanceState.getString("passwordSave"))
    }
}