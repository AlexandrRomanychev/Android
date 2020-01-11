package com.example.contacts.async;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.contacts.LoginActivity;
import com.example.contacts.database.AppDatabase;

public class AsyncEnterUser extends AsyncTask<Void, Void, Integer> {

    private AppDatabase db;
    private Context activity;
    private String login, password;

    public AsyncEnterUser(Context activity, AppDatabase db, String login, String password){
        this.activity = activity;
        this.db = db;
        this.login = login;
        this.password = password;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return db.userDao().findCountUsers(login ,password);
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (result > 0) {
            Toast.makeText(activity, "Успешный вход", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, "Неверный логин/пароль", Toast.LENGTH_SHORT).show();
        }
    }
}
