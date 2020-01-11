package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.contacts.async.AsyncEnterUser;
import com.example.contacts.async.AsyncRegistryUser;
import com.example.contacts.database.AppDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText login, password;
    private Button enter,registry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);

        enter = findViewById(R.id.authorization);
        registry = findViewById(R.id.registration);

        final AppDatabase db  = Room.inMemoryDatabaseBuilder(MainActivity.this, AppDatabase.class).build();

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncEnterUser(MainActivity.this, db, login.getText().toString(), password.getText().toString()).execute();
            }
        });

        registry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncRegistryUser(MainActivity.this, db, login.getText().toString(), password.getText().toString()).execute();
            }
        });
    }
}
