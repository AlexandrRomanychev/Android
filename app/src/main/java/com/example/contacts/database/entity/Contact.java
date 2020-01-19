package com.example.contacts.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contact {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "phone")
    public String phone;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "photo")
    public String photo;

    public Contact(String name, String date, String phone,
                   String photo){
        this.name = name;
        this.date = date;
        this.phone = phone;
        this.photo = photo;
    }
    public Contact(String name, String date, String phone,
                   String photo, int id){
        this.name = name;
        this.date = date;
        this.phone = phone;
        this.photo = photo;
        this.uid = id;
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
