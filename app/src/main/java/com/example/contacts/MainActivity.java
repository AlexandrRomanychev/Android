package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.contacts.async.AsyncCheckRegistryUser;
import com.example.contacts.async.AsyncEnterUser;
import com.example.contacts.database.AppDatabase;
import com.example.contacts.database.entity.User;
import com.facebook.drawee.backends.pipeline.Fresco;

public class MainActivity extends AppCompatActivity {

    private EditText login, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);

        Button enter = findViewById(R.id.authorization);
        Button registry = findViewById(R.id.registration);

        final AppDatabase db  = Room.inMemoryDatabaseBuilder(MainActivity.this, AppDatabase.class).build();

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

    public static class MyFresco extends Application {

        @Override
        public void onCreate() {
            super.onCreate();
            Fresco.initialize(this);

        }
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
