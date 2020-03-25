package com.example.contacts.async

import android.os.AsyncTask
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.contacts.Profile
import com.example.contacts.UploadWorker
import com.example.contacts.database.AppDatabase
import com.example.contacts.database.DataBaseComands
import com.example.contacts.database.converter.DateConverter
import com.example.contacts.database.converter.DateConverter.dateToString
import com.example.contacts.database.entity.Contact
import java.util.*

class AsyncContactAction(private val db: AppDatabase, private val activity: Profile?, private val contact: Contact?, private val rule: String?, private val status: DataBaseComands, worker: UploadWorker?) : AsyncTask<Void?, Void?, Int>() {
    private var contacts: List<Contact>?
    private val worker: UploadWorker?

    override fun doInBackground(vararg params: Void?): Int? {
        when (status) {
            DataBaseComands.CONTACT_ADD -> {
                db.contactDao()!!.insertAll(contact)
            }
            DataBaseComands.CONTACT_DELETE -> {
                db.contactDao()!!.delete(contact)
            }
            DataBaseComands.CONTACT_GET_ALL, DataBaseComands.CONTACT_CONGRATULATE -> {
                contacts = db.contactDao()!!.getAll(rule)
            }
            DataBaseComands.CONTACT_SORT_NAME_UP -> {
                contacts = db.contactDao()!!.getSortedByNameUp(rule)
            }
            DataBaseComands.CONTACT_SORT_NAME_DOWN -> {
                contacts = db.contactDao()!!.getSortedByNameDown(rule)
            }
            DataBaseComands.CONTACT_SORT_DATE_UP -> {
                contacts = db.contactDao()!!.getSortedByDateUp(rule)
            }
            DataBaseComands.CONTACT_SORT_DATE_DOWN -> {
                contacts = db.contactDao()!!.getSortedByDateDown(rule)
            }
            DataBaseComands.CONTACT_UPDATE -> {
                db.contactDao()!!.updateContact(contact!!.name, contact.date, contact.phone, contact.photo, intArrayOf(contact.uid))
            }
        }
        return 1
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onPostExecute(result: Int) {
        when (status) {
            DataBaseComands.CONTACT_GET_ALL, DataBaseComands.CONTACT_SORT_NAME_UP, DataBaseComands.CONTACT_SORT_NAME_DOWN, DataBaseComands.CONTACT_SORT_DATE_UP, DataBaseComands.CONTACT_SORT_DATE_DOWN -> {
                activity!!.showListOfProfiles(contacts)
            }
            DataBaseComands.CONTACT_CONGRATULATE -> {
                val date = Date()
                val strDate = dateToString(date.time)
                val stringBuffer = StringBuffer(strDate!!)
                val contactsBirthday: MutableList<Contact> = ArrayList()
                for (contact in contacts!!) {
                    if (dateToString(contact!!.date)!!.contains(stringBuffer.delete(5, 10).toString())) contactsBirthday.add(contact)
                }
                worker!!.setContactList(contactsBirthday)
            }
        }
    }

    init {
        contacts = ArrayList()
        this.worker = worker
    }
}