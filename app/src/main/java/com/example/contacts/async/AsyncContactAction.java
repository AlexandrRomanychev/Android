package com.example.contacts.async;

import android.app.Activity;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.example.contacts.Profile;
import com.example.contacts.UploadWorker;
import com.example.contacts.database.AppDatabase;
import com.example.contacts.database.DataBaseComands;
import com.example.contacts.database.converter.DateConverter;
import com.example.contacts.database.entity.Contact;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AsyncContactAction  extends AsyncTask<Void, Void, Integer> {

    private final AppDatabase db;
    private final Contact contact;
    private DataBaseComands status;
    private final String rule;
    private List<Contact> contacts;
    private final Profile activity;
    private final UploadWorker worker;
    private String login;

    public AsyncContactAction(AppDatabase db, Profile activity, Contact contact, String rule, DataBaseComands status, String login, UploadWorker worker){
        this.db = db;
        this.contact = contact;
        this.status = status;
        this.activity = activity;
        this.rule = rule;
        this.contacts = new ArrayList<>();
        this.login = login;
        this.worker = worker;
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
            case CONTACT_GET_ALL:
            case CONTACT_CONGRATULATE:{
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
                break;
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
                break;
            }
            case CONTACT_CONGRATULATE:{
                Date date = new Date();
                String strDate = new DateConverter().dateToString(date.getTime());
                StringBuffer stringBuffer = new StringBuffer(strDate);
                List<Contact> contactsBirthday = new ArrayList<>();
                for (Contact contact: contacts) {
                    if (contact.getDate().contains(stringBuffer.delete(5,10).toString()))
                        contactsBirthday.add(contact);
                }
                worker.setContactList(contactsBirthday);
            }
        }

    }
}
