package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.contacts.async.AsyncCheckRegistryUser;
import com.example.contacts.async.AsyncEnterUser;
import com.example.contacts.database.AppDatabase;
import com.example.contacts.database.entity.User;

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

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncEnterUser(MainActivity.this, db,
                        new User(login.getText().toString(), password.getText().toString()),
                        Profile.class).execute();
            }
        });

        registry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncCheckRegistryUser(MainActivity.this, db,
                        new User(login.getText().toString(), password.getText().toString()),
                        Profile.class).execute();
            }
        });
    }
}
