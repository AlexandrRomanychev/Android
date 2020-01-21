package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.contacts.async.AsyncUserAction;
import com.example.contacts.database.AppDatabase;
import com.example.contacts.database.DataBaseComands;
import com.example.contacts.validation.Validation;

public class MainActivity extends AppCompatActivity {

    private EditText login, password;
    public static AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(MainActivity.this, AppDatabase.class, "contacts").build();

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);

        Button enter = findViewById(R.id.authorization);
        Button registry = findViewById(R.id.registration);

        new AsyncUserAction(MainActivity.this, db, null, Profile.class, DataBaseComands.USER_GET_LOGINED).execute();

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation.validateLoginPage(MainActivity.this, login.getText().toString(),
                        password.getText().toString(), DataBaseComands.USER_ENTER);
            }
        });

        registry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation.validateLoginPage(MainActivity.this, login.getText().toString(),
                        password.getText().toString(), DataBaseComands.USER_CHECK_REGISTRY);
            }
        });
    }

    // сохранение состояния
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString("loginSave", login.getText().toString());
        outState.putString("passwordSave", password.getText().toString());

        super.onSaveInstanceState(outState);
    }

    // получение сохраненного состояния
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        login.setText(savedInstanceState.getString("loginSave"));
        password.setText(savedInstanceState.getString("passwordSave"));
    }
}
