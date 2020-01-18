package com.example.contacts.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.contacts.database.AppDatabase;
import com.example.contacts.database.entity.Contact;

import java.util.List;

public class AsyncAddContact  extends AsyncTask<Void, Void, Integer> {

    private final Context activity;
    private final AppDatabase db;
    private final Contact contact;

    public AsyncAddContact(Context activity, AppDatabase db, Contact contact){
        this.activity = activity;
        this.db = db;
        this.contact = contact;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        db.contactDao().insertAll(contact);
        List<Contact> contacts = db.contactDao().getAll();
        return 1;
    }
}
