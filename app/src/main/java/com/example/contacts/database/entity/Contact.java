package com.example.contacts.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.contacts.database.converter.DateConverter;

@Entity
public class Contact {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "phone")
    public String phone;

    @ColumnInfo(name = "date")
    public long date;

    @ColumnInfo(name = "photo")
    public String photo;

    @ColumnInfo(name = "login")
    public String login;

    public Contact(String name, long date, String phone,
                   String photo, String login){
        this.name = name;
        this.date = date;
        this.phone = phone;
        this.photo = photo;
        this.login = login;
    }

    public Contact(String name, long date, String phone,
                   String photo, int id, String login){
        this.name = name;
        this.date = date;
        this.phone = phone;
        this.photo = photo;
        this.uid = id;
        this.login = login;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return DateConverter.dateToString(date);
    }

    public void setDate(String date) {
        this.date = DateConverter.dateToTimestamp(date);
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
