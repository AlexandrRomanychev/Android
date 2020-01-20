package com.example.contacts.validation;

import android.widget.Toast;
import android.content.Context;

import com.example.contacts.MainActivity;
import com.example.contacts.Profile;
import com.example.contacts.async.AsyncUserAction;
import com.example.contacts.database.DataBaseComands;
import com.example.contacts.database.entity.User;

public class Validation {

    private static final String DATE_PATTERN =
            "(0?[1-9]|1[012]) [/.-] (0?[1-9]|[12][0-9]|3[01]) [/.-] ((19|20)\\d\\d)";

    public static void validateLoginPage(Context activity, String login,
                                         String password, DataBaseComands status){
        if (validateString(login))
            Toast.makeText(activity, "Некорректный логин", Toast.LENGTH_SHORT).show();
        else if (validateString(password))
            Toast.makeText(activity, "Некорректный пароль", Toast.LENGTH_SHORT).show();
        else {
            new AsyncUserAction(activity, MainActivity.db,
                    new User(login, password),
                    Profile.class, status).execute();
        }
    }

    private static boolean validateDateFormat(String date){
        return date.matches(DATE_PATTERN);
    }

    private static boolean validateString(String item){
        return item.trim().equals("");
    }
}
