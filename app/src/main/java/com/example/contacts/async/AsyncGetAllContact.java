package com.example.contacts.async;

import android.os.AsyncTask;

import com.example.contacts.database.AppDatabase;
import com.example.contacts.database.entity.Contact;

import java.util.ArrayList;
import java.util.List;

public class AsyncGetAllContact  extends AsyncTask<Void, Void, Integer> {

    private final AppDatabase db;
    private List<Contact> contacts;

    public List<Contact> getContacts(){return this.contacts;}

    public AsyncGetAllContact(AppDatabase db){
        this.db = db;
        contacts = new ArrayList<>();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        contacts = db.contactDao().getAll();
        return 1;
    }
}
