package com.example.contacts;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.WorkManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;


public class MyFresco extends Application {

    public static final String CHANNEL_ID = "Push_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        // Уведомление один раз в день
        PeriodicWorkRequest pushRequest =
                new PeriodicWorkRequest.Builder(UploadWorker.class, 1, TimeUnit.DAYS)
                        .setConstraints(constraints)
                        .build();

        WorkManager workManager = WorkManager.getInstance(this);
        workManager.enqueueUniquePeriodicWork(CHANNEL_ID, ExistingPeriodicWorkPolicy.REPLACE, pushRequest);

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        /////////  Для передачи параметров есть объект Data и метод putString(может для login потребуется):
        //                                                           https://codelabs.developers.google.com/codelabs/android-workmanager/#4
    }
}
