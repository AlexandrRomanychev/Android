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

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();

        PeriodicWorkRequest saveRequest =
                new PeriodicWorkRequest.Builder(UploadWorker.class, 1, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(this).enqueue(saveRequest);
    }
}
