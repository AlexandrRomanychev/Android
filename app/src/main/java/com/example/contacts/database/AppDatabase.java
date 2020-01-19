package com.example.contacts.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.contacts.database.dao.ContactDao;
import com.example.contacts.database.dao.UserDao;
import com.example.contacts.database.entity.Contact;
import com.example.contacts.database.entity.User;

@Database(entities = {User.class, Contact.class}, version =  1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ContactDao contactDao();
}
