package com.example.contacts.validation;

import android.widget.Toast;
import android.content.Context;
import com.example.contacts.MainActivity;
import com.example.contacts.Profile;
import com.example.contacts.async.AsyncUserAction;
import com.example.contacts.database.AppDatabase;
import com.example.contacts.database.DataBaseComands;
import com.example.contacts.database.entity.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Validation {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public static void validateLoginPage(Context activity, String login,
                                         String password, DataBaseComands status, AppDatabase db){
        if (validateString(login))
            Toast.makeText(activity, "Некорректный логин", Toast.LENGTH_SHORT).show();
        else if (validateString(password))
            Toast.makeText(activity, "Некорректный пароль", Toast.LENGTH_SHORT).show();
        else {
            new AsyncUserAction(activity, db,
                    new User(login, password),
                    Profile.class, status).execute();
        }
    }

    public static boolean validateContactPage(Context activity, String name, String date,
                                           String phone){
        if (validateString(name)) {
            Toast.makeText(activity, "Поле ФИО не заполнено", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (validateString(phone)) {
            Toast.makeText(activity, "Поле Телефон не заполнено", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (validateString(date)) {
            Toast.makeText(activity, "Поле Дата не заполнено", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!validateDateFormat(date)){
            Toast.makeText(activity, "Дата имеет неверный формат", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private static boolean validateDateFormat(String date){
        try {
            return DATE_FORMAT.format(DATE_FORMAT.parse(date)).equals(date);
        }catch (ParseException ex){
            return false;
        }
    }

    private static boolean validateString(String item){
        return item.trim().equals("");
    }
}
