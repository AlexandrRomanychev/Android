package com.example.contacts.async;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.contacts.database.AppDatabase;
import com.example.contacts.database.entity.User;

public class AsyncEnterUser extends AsyncTask<Void, Void, Integer> {

    private final AppDatabase db;
    private final Context activity;
    private final User user;
    private final Class nextPage;

    public AsyncEnterUser(Context activity, AppDatabase db, User user, Class nextPage){
        this.activity = activity;
        this.db = db;
        this.user = user;
        this.nextPage = nextPage;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return db.userDao().findCountUsers(user.getLogin(), user.getPassword());
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (result > 0) {
            Toast.makeText(activity, "Успешный вход", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, nextPage);
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, "Неверный логин/пароль", Toast.LENGTH_SHORT).show();
        }
    }
}
