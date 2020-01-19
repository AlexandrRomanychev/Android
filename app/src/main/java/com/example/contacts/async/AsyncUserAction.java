package com.example.contacts.async;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.contacts.database.AppDatabase;
import com.example.contacts.database.DataBaseComands;
import com.example.contacts.database.entity.LogUser;
import com.example.contacts.database.entity.User;

public class AsyncUserAction extends AsyncTask<Void, Void, Integer> {

    private final AppDatabase db;
    private final Context activity;
    private final User user;
    private final Class nextPage;
    private DataBaseComands status;
    private LogUser logUser;

    public AsyncUserAction(Context activity, AppDatabase db, User user, Class nextPage, DataBaseComands status){
        this.activity = activity;
        this.db = db;
        this.user = user;
        this.nextPage = nextPage;
        this.status = status;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        switch (status){
            case USER_ENTER: {
                return db.userDao().findCountUsers(user.getLogin(), user.getPassword());
            }
            case USER_CHECK_REGISTRY: {
                return db.userDao().findUserByLogin(user.getLogin());
            }
            case USER_REGISTRY: {
                db.userDao().insertAll(user);
                break;
            }
            case USER_ADD:{
                db.logUserDao().insertAll(new LogUser(user.getLogin(), user.getPassword()));
                break;
            }
            case USER_GET_LOGINED:{
                logUser = db.logUserDao().getLogUser();
                break;
            }
            case USER_DELETE:{
                db.logUserDao().deleteAll(user.getLogin());
                break;
            }
            default: {
                return 0;
            }
        }
        return 0;
    }

    @Override
    protected void onPostExecute(Integer result) {
        switch (status){
            case USER_ENTER: {
                if (result > 0) {
                    Toast.makeText(activity, "Успешный вход", Toast.LENGTH_SHORT).show();
                    new AsyncUserAction(activity, db, user, nextPage, DataBaseComands.USER_ADD).execute();
                    /*Intent intent = new Intent(activity, nextPage);
                    activity.startActivity(intent);*/
                } else {
                    Toast.makeText(activity, "Неверный логин/пароль", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case USER_CHECK_REGISTRY: {
                if (result > 0) {
                    Toast.makeText(activity, "Пользователь с таким логином уже существует!", Toast.LENGTH_SHORT).show();
                } else {
                    new AsyncUserAction(activity, db, user, nextPage, DataBaseComands.USER_REGISTRY).execute();
                }
                break;
            }
            case USER_REGISTRY:{
                Toast.makeText(activity, "Успешная регистрация", Toast.LENGTH_SHORT).show();
                new AsyncUserAction(activity, db, user, nextPage, DataBaseComands.USER_ADD).execute();
                /*Intent intent = new Intent(activity, nextPage);
                activity.startActivity(intent);*/
                break;
            }
            case USER_GET_LOGINED:{
                if (logUser != null){
                    Intent intent = new Intent(activity, nextPage);
                    intent.putExtra("login", logUser.getLogin());
                    activity.startActivity(intent);
                }
                break;
            }
            case USER_ADD:{
                new AsyncUserAction(activity, db, user, nextPage, DataBaseComands.USER_GET_LOGINED).execute();
                /*Intent intent = new Intent(activity, nextPage);
                intent.putExtra("login", logUser.getLogin());
                activity.startActivity(intent);*/
                break;
            }
        }
    }
}
