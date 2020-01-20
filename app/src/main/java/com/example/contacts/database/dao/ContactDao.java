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
    @Query("SELECT * FROM contact WHERE name LIKE :rule AND login = :login")
    List<Contact> getAll(String rule, String login);

    @Query("SELECT * FROM contact WHERE login = :login AND name LIKE :rule ORDER BY name ASC")
    List<Contact> getSortedByNameUp(String rule, String login);

    @Query("SELECT * FROM contact WHERE login = :login AND name LIKE :rule ORDER BY name DESC")
    List<Contact> getSortedByNameDown(String rule, String login);

    @Query("SELECT * FROM contact WHERE login = :login AND name LIKE :rule ORDER BY date ASC")
    List<Contact> getSortedByDateUp(String rule, String login);

    @Query("SELECT * FROM contact WHERE login = :login AND name LIKE :rule ORDER BY date DESC")
    List<Contact> getSortedByDateDown(String rule, String login);

    @Query("UPDATE contact SET name = :name, date = :date, phone = :phone, photo = :photo WHERE uid IN (:contactIds) AND login = :login")
    void updateContact(String name, String date, String phone, String photo, int[] contactIds, String login);

    @Insert
    void insertAll(Contact... contacts);

    @Delete
    void delete(Contact contact);
}
