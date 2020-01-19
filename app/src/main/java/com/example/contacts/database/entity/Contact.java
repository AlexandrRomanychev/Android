package com.example.contacts.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.net.Uri;

@Entity
public class Contact {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "surname")
    public String surname;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "patronymic")
    public String patronymic;

    @ColumnInfo(name = "phone")
    public String phone;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "photo")
    public String photo;

    public Contact(String surname, String name, String patronymic, String date, String phone,
                   String photo){
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.date = date;
        this.phone = phone;
        this.photo = photo;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
