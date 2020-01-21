package com.example.contacts;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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
    }

    public void setContactList(List<Contact> contacts){
        this.contactList.addAll(contacts);
    }

    @Override
    public Result doWork() {
        // Do the work here--in this case, upload the images.

        // Do the work here--in this case, upload the images.

        //////////////////////////////////////////////////////////////////
        ////////////////// АЛГОРИТМ PUSH - УВЕДОМЛЕНИЙ( вер 1.0)//////////

        for (Contact contact: contactList) {

            //1. Получить список контактов, у которых сегодня день рождения

            //2. В цикле от [0 до countProfiles) do

            //{

            Context context = getApplicationContext();
            Intent pushCall = new Intent(Intent.ACTION_DIAL); // дисплей с уже набранным номером телефона с заполненным человеком
            pushCall.setData(Uri.parse("tel: "+contact.phone)); // здесь телефон: pushCall.setData(Uri.parse("tel: + profile.phone"))
            PendingIntent callPendingIntent = PendingIntent.getBroadcast(context, 0, pushCall, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MyFresco.CHANNEL_ID)
                    .setSmallIcon(R.drawable.plus) // profile.uri и т.д. Если не получится, то и без картинки можно
                    .setContentTitle("Сегодня день рождения!!!")
                    .setContentText("Позвоните именниннику торжества по имени ...!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(callPendingIntent)
                    .addAction(R.drawable.exit, "Позвонить", callPendingIntent);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            // notificationId is a unique int for each notification that you must define
            // NOTIFY_ID должен быть уникальным для каждого профиля. NOTIFY_ID должен равняться profile.uid
            //notificationManager.notify(NOTIFY_ID, builder.build());
            notificationManager.notify(contact.getUid(), builder.build());

            //}
        }

        //P.S
        ///////// Надеюсь дорогой читатель ты оценишь оригинальность :D

        return Result.success();
    }
}
