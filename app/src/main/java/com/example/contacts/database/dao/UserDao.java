package com.example.contacts.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.contacts.database.entity.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT COUNT(*) FROM user WHERE login = :login AND password = :password")
    Integer findCountUsers(String login, String password);

    @Query("SELECT COUNT(*) FROM user WHERE login = :login")
    Integer findUserByLogin(String login);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}
