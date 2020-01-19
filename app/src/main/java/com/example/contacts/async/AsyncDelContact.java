package com.example.contacts.async;

import android.os.AsyncTask;

import com.example.contacts.database.AppDatabase;
import com.example.contacts.database.entity.Contact;

public class AsyncDelContact  extends AsyncTask<Void, Void, Integer> {

    private final AppDatabase db;
    private final Contact contact;

    public AsyncDelContact(AppDatabase db, Contact contact){
        this.db = db;
        this.contact = contact;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        db.contactDao().delete(contact);
        return 1;
    }
}
