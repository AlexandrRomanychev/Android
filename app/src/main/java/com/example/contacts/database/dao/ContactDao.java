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
    @Query("SELECT * FROM contact WHERE name LIKE :rule")
    List<Contact> getAll(String rule);

    @Query("SELECT * FROM contact ORDER BY name ASC")
    List<Contact> getSortedByNameUp();

    @Query("SELECT * FROM contact ORDER BY name DESC")
    List<Contact> getSortedByNameDown();

    @Query("SELECT * FROM contact WHERE uid IN (:contactIds)")
    List<User> loadAllByIds(int[] contactIds);

    @Insert
    void insertAll(Contact... contacts);

    @Delete
    void delete(Contact contact);
}
