package com.example.contacts.async;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.contacts.database.AppDatabase;
import com.example.contacts.database.entity.User;

public class AsyncCheckRegistryUser extends AsyncTask<Void, Void, Integer> {

    private final AppDatabase db;
    private final Context activity;
    private final User user;
    private final Class nextPage;

    public AsyncCheckRegistryUser(Context activity, AppDatabase db, User user, Class nextPage){
        this.activity = activity;
        this.db = db;
        this.user = user;
        this.nextPage = nextPage;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return db.userDao().findUserByLogin(user.getLogin());
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (result > 0) {
            Toast.makeText(activity, "Пользователь с таким логином уже существует!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Успешная регистрация", Toast.LENGTH_SHORT).show();
            new AsyncRegistryUser(db, user).execute();
            Intent intent = new Intent(activity, nextPage);
            activity.startActivity(intent);
        }
    }
}
