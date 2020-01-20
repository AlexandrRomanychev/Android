package com.example.contacts.async;

import android.os.AsyncTask;

import com.example.contacts.Profile;
import com.example.contacts.database.AppDatabase;
import com.example.contacts.database.DataBaseComands;
import com.example.contacts.database.converter.DateConverter;
import com.example.contacts.database.entity.Contact;

import java.util.ArrayList;
import java.util.List;

public class AsyncContactAction  extends AsyncTask<Void, Void, Integer> {

    private final AppDatabase db;
    private final Contact contact;
    private DataBaseComands status;
    private final String rule;
    private List<Contact> contacts;
    private final Profile activity;
    private String login;

    public AsyncContactAction(AppDatabase db, Profile activity, Contact contact, String rule, DataBaseComands status, String login){
        this.db = db;
        this.contact = contact;
        this.status = status;
        this.activity = activity;
        this.rule = rule;
        this.contacts = new ArrayList<>();
        this.login = login;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        switch (status){
            case CONTACT_ADD: {
                db.contactDao().insertAll(contact);
                break;
            }
            case CONTACT_DELETE:{
                db.contactDao().delete(contact);
                break;
            }
            case CONTACT_GET_ALL:{
                contacts = db.contactDao().getAll(rule, login);
                break;
            }
            case CONTACT_SORT_NAME_UP:{
                contacts = db.contactDao().getSortedByNameUp(rule, login);
                break;
            }
            case CONTACT_SORT_NAME_DOWN:{
                contacts = db.contactDao().getSortedByNameDown(rule, login);
                break;
            }
            case CONTACT_SORT_DATE_UP:{
                contacts = db.contactDao().getSortedByDateUp(rule, login);
                break;
            }
            case CONTACT_SORT_DATE_DOWN:{
                contacts = db.contactDao().getSortedByDateDown(rule, login);
                break;
            }
            case CONTACT_UPDATE:{
                db.contactDao().updateContact(contact.name, contact.date, contact.phone, contact.photo, new int[]{contact.uid}, login);
            }
        }
        return 1;
    }

    @Override
    protected void onPostExecute(Integer result) {
        switch (status){
            case CONTACT_GET_ALL:
            case CONTACT_SORT_NAME_UP:
            case CONTACT_SORT_NAME_DOWN:
            case CONTACT_SORT_DATE_UP:
            case CONTACT_SORT_DATE_DOWN:{
                activity.showListOfProfiles(this.contacts);
            }
        }

    }
}
