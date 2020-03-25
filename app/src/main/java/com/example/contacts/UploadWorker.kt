package com.example.contacts

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.contacts.Profile.Companion.login
import com.example.contacts.async.AsyncContactAction
import com.example.contacts.database.AppDatabase
import com.example.contacts.database.DataBaseComands
import com.example.contacts.database.entity.Contact
import java.util.*

class UploadWorker(
        context: Context,
        params: WorkerParameters) : Worker(context, params) {
    private val contactList: MutableList<Contact>
    private val db: AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "contacts").build()
    fun setContactList(contacts: MutableList<Contact>) {
        contactList.addAll(contacts)
        for (contact in contactList) {
            val context = applicationContext
            val pushCall = Intent(Intent.ACTION_DIAL) // дисплей с уже набранным номером телефона с заполненным человеком
            pushCall.data = Uri.parse("tel:" + contact.phone) // здесь телефон: pushCall.setData(Uri.parse("tel: + profile.phone"))
            val callPendingIntent = PendingIntent.getActivity(context, 0, pushCall, 0)
            val builder = NotificationCompat.Builder(context, MyFresco.CHANNEL_ID)
                    .setSmallIcon(R.drawable.plus) // profile.uri и т.д. Если не получится, то и без картинки можно
                    .setContentTitle("Сегодня день рождения у " + contact.name + " !!!")
                    .setContentText("Позвоните именниннику и поздравьте его!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(callPendingIntent)
                    .addAction(R.drawable.exit, "Позвонить", callPendingIntent)
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(contact.uid, builder.build())
        }
    }

    @SuppressLint("WrongThread")
    override fun doWork(): Result {
        /*for (contact in contactList) {
            val context = applicationContext
            val pushCall = Intent(Intent.ACTION_DIAL) // дисплей с уже набранным номером телефона с заполненным человеком
            pushCall.data = Uri.parse("tel:" + contact.phone) // здесь телефон: pushCall.setData(Uri.parse("tel: + profile.phone"))
            val callPendingIntent = PendingIntent.getActivity(context, 0, pushCall, 0)
            val builder = NotificationCompat.Builder(context, MyFresco.CHANNEL_ID)
                    .setSmallIcon(R.drawable.plus) // profile.uri и т.д. Если не получится, то и без картинки можно
                    .setContentTitle("Сегодня день рождения у " + contact.name + " !!!")
                    .setContentText("Позвоните именниннику и поздравьте его!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(callPendingIntent)
                    .addAction(R.drawable.exit, "Позвонить", callPendingIntent)
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(contact.uid, builder.build())
        }*/
        AsyncContactAction(db, null, null, "%", DataBaseComands.CONTACT_CONGRATULATE, this).execute()
        return Result.success()
    }

    companion object {
        private const val NOTIFY_ID = 101
    }

    init {
        contactList = ArrayList()
    }
}