package com.example.contacts.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.contacts.database.entity.LogUser;

import java.util.List;

@Dao
public interface LogUserDao {
    @Query("SELECT * FROM loguser LIMIT 1")
    LogUser getLogUser();

    @Insert
    void insertAll(LogUser users);

    @Query("DELETE FROM loguser WHERE login = :login")
    void deleteAll(String login);
}
