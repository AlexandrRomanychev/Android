package com.example.contacts

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.facebook.drawee.backends.pipeline.Fresco
import java.util.concurrent.TimeUnit

class MyFresco : Application() {
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        // Create the NotificationChannel, but only on API 26+ because
// the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.channel_name)
            val description = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
// or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
        val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .build()
        // Уведомление один раз в день
        val pushRequest = PeriodicWorkRequest.Builder(UploadWorker::class.java, 1, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()
        val workManager = WorkManager.getInstance(this)
        workManager.enqueueUniquePeriodicWork(CHANNEL_ID, ExistingPeriodicWorkPolicy.REPLACE, pushRequest)
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
/////////  Для передачи параметров есть объект Data и метод putString(может для login потребуется):
//                                                           https://codelabs.developers.google.com/codelabs/android-workmanager/#4
    }

    companion object {
        const val CHANNEL_ID = "Push_channel"
    }
}