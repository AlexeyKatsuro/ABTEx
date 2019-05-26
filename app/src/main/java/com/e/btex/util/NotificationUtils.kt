package com.e.btex.util

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.e.btex.R



object NotificationUtils {


    fun createNotificationCompat(context: Service, notificationId: Int, channelId: String) {
        val notification = buildNotification(context, channelId)
        context.startForeground(notificationId, notification)
    }

    private fun buildNotification(
        context: Service, channelId: String
    ): Notification {

        if (Build.VERSION.SDK_INT >= 26) createChannel(context, channelId)

        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_content))
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()
    }

    @TargetApi(26)
    private fun createChannel(ctx: Service, channelId: String): String {
        // Create a channel.
        val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelName = "BTEx channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val notificationChannel = NotificationChannel(channelId, channelName, importance)

        notificationManager.createNotificationChannel(notificationChannel)
        return channelId
    }
}