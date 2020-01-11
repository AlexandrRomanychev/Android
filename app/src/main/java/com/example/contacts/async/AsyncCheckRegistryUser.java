package com.example.contacts.async;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.contacts.LoginActivity;
import com.example.contacts.database.AppDatabase;
import com.example.contacts.database.entity.User;

public class AsyncCheckRegistryUser extends AsyncTask<Void, Void, Integer> {

    private AppDatabase db;
    private Context activity;
    private String login, password;

    public AsyncCheckRegistryUser(Context activity, AppDatabase db, String login, String password){
        this.activity = activity;
        this.db = db;
        this.login = login;
        this.password = password;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return db.userDao().findUserByLogin(login);
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (result > 0) {
            Toast.makeText(activity, "Пользователь с таким логином уже существует!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Успешная регистрация", Toast.LENGTH_SHORT).show();
            db.userDao().delete(new User(login, password));
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
        }
    }
}
