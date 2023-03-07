package com.emmanuelamet.dogs.nofication

import android.Manifest
import android.app.Notification
import android.app.Notification.BigPictureStyle
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.emmanuelamet.dogs.R
import com.emmanuelamet.dogs.view.MainActivity

class NotificationHelper(val context: Context) {

    private val CHANNEL_ID = "Dogs channel id"
    private val NOTIFICATION_ID = 123

    fun createNotification(){
        createNotificationChannel()

        val intent = Intent(context, MainActivity::class.java).apply {
            flags - Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val icon = BitmapFactory.decodeResource(context.resources, R.drawable.dog_icon)
        val notification = Notification.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.dog_icon)
            .setLargeIcon(icon)
            .setContentText("Dogs retrieved")
            .setContentText("This is a list of dog breeds")
            .setStyle(
                BigPictureStyle()
                    .bigPicture(icon)
                    .bigLargeIcon(icon)
            )
            .setContentIntent(pendingIntent)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = CHANNEL_ID
            val descriptionText = "Channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}