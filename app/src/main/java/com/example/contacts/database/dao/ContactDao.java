package com.example.contacts.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.contacts.database.entity.Contact;
import com.example.contacts.database.entity.User;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contact")
    List<Contact> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT COUNT(*) FROM user WHERE login = :login AND password = :password")
    Integer findCountUsers(String login, String password);

    @Query("SELECT COUNT(*) FROM user WHERE login = :login")
    Integer findUserByLogin(String login);

    @Insert
    void insertAll(Contact... contacts);

    @Delete
    void delete(Contact contact);
}
