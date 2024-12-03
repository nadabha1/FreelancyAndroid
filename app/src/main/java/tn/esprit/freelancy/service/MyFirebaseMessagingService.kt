package tn.esprit.freelancy.service

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log
import androidx.core.app.NotificationCompat
import tn.esprit.freelancy.R

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Check if message contains a notification
        remoteMessage.notification?.let {
            Log.d("FCM", "Message Notification Body: ${it.body}")
            // Display the notification
            showNotification(it.body)
        }
    }

    @SuppressLint("ServiceCast")
    private fun showNotification(message: String?) {
        // Show the notification to the entrepreneur
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, "project_notification_channel")
            .setContentTitle("New Application")
            .setContentText(message)
            .setSmallIcon(R.drawable.img)
            .build()

        notificationManager.notify(0, notification)
    }


    fun sendNotification(context: Context, message: String) {
        val notification = NotificationCompat.Builder(context, "channel_id")
            .setContentTitle("New Application")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

}
