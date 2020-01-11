package com.example.contacts.async;

import android.os.AsyncTask;
import com.example.contacts.database.AppDatabase;
import com.example.contacts.database.entity.User;

public class AsyncRegistryUser extends AsyncTask<Void, Void, Integer> {

    private final AppDatabase db;
    private final User user;

    public AsyncRegistryUser(AppDatabase db, User user){
        this.db = db;
        this.user = user;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        db.userDao().insertAll(user);
        return 0;
    }
}
