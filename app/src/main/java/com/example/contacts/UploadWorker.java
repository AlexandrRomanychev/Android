package com.example.contacts;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.contacts.async.AsyncContactAction;
import com.example.contacts.database.AppDatabase;
import com.example.contacts.database.DataBaseComands;
import com.example.contacts.database.entity.Contact;

import java.util.ArrayList;
import java.util.List;

public class UploadWorker extends Worker {

    private static final int NOTIFY_ID = 101;
    private List<Contact> contactList;

    public UploadWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        contactList = new ArrayList<>();
        AppDatabase db = db = Room.databaseBuilder(context, AppDatabase.class, "contacts").build();
        new AsyncContactAction(db, null,null, "%", DataBaseComands.CONTACT_CONGRATULATE,Profile.getLogin(),this).execute();
    }

    public void setContactList(List<Contact> contacts){
        this.contactList.addAll(contacts);
    }

    @Override
    public Result doWork() {
        for (Contact contact: contactList) {
            Context context = getApplicationContext();
            Intent pushCall = new Intent(Intent.ACTION_DIAL); // дисплей с уже набранным номером телефона с заполненным человеком
            pushCall.setData(Uri.parse("tel:"+contact.phone)); // здесь телефон: pushCall.setData(Uri.parse("tel: + profile.phone"))
            PendingIntent callPendingIntent = PendingIntent.getBroadcast(context, 0, pushCall, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MyFresco.CHANNEL_ID)
                    .setSmallIcon(R.drawable.plus) // profile.uri и т.д. Если не получится, то и без картинки можно
                    .setContentTitle("Сегодня день рождения у "+contact.getName()+" !!!")
                    .setContentText("Позвоните именниннику и поздравьте его!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(callPendingIntent)
                    .addAction(R.drawable.exit, "Позвонить", callPendingIntent);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            notificationManager.notify(contact.getUid(), builder.build());
        }
        return Result.success();
    }
}
