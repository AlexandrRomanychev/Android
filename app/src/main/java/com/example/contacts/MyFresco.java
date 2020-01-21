package com.example.contacts;

import android.app.Application;
import android.content.Context;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.WorkManager;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;


public class MyFresco extends Application {

    public static final String CHANNEL_ID = "Push_chanlel";

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);


        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        // Уведомление один раз в день
        PeriodicWorkRequest pushRequest =
                new PeriodicWorkRequest.Builder(UploadWorker.class, 1, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build();

        WorkManager workManager = WorkManager.getInstance(this);
        workManager.enqueueUniquePeriodicWork(CHANNEL_ID, ExistingPeriodicWorkPolicy.REPLACE, pushRequest);

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        /////////  Для передачи параметров есть объект Data и метод putString(может для login потребуется):
        //                                                           https://codelabs.developers.google.com/codelabs/android-workmanager/#4
    }
}
