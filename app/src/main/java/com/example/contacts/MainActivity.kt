package com.example.contacts

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.contacts.database.AppDatabase

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