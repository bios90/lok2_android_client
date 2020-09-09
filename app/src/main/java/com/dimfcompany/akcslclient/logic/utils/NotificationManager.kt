package com.dimfcompany.akcslclient.logic.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.AppClass
import com.dimfcompany.akcslclient.base.BusMainEvents
import com.dimfcompany.akcslclient.base.Constants
import com.dimfcompany.akcslclient.base.enums.TypePush
import com.dimfcompany.akcslclient.base.extensions.*
import com.dimfcompany.akcslclient.logic.SharedPrefsManager
import com.dimfcompany.akcslclient.ui.screens.act_main.ActMain
import com.google.firebase.messaging.RemoteMessage
import java.io.Serializable
import java.lang.RuntimeException
import javax.inject.Inject

class MyPush(val type: TypePush,
             val document_id: Long? = null,
             val document_name: String? = null,
             val category_id: Long? = null) : Serializable
{
    companion object
    {
        fun initFromRemoteMessage(remote: RemoteMessage): MyPush?
        {
            val type_str = remote.getString("type") ?: return null
            val type = TypePush.initFromSting(type_str) ?: return null

            val document_id = remote.getLong("document_id")
            val document_name = remote.getString("document_name")
            val category_id = remote.getLong("category_id")

            return MyPush(type, document_id, document_name, category_id)
        }
    }

    fun getTitle(): String
    {
        return when (type)
        {
            TypePush.NEW_DOCUMENT -> "Добавлен новый документ \"$document_name\""
            TypePush.UPDATE_DOCUMENT -> "Документ \"$document_name\" обновлен"
            else -> throw RuntimeException("**** Error not showing notify for this type ****")
        }
    }
}

class NotificationManager
{
    @Inject
    lateinit var bus_main_events: BusMainEvents

    init
    {
        AppClass.app_component.inject(this)
    }

    fun notify(remoteMessage: RemoteMessage)
    {
        val my_push = MyPush.initFromRemoteMessage(remoteMessage) ?: return
        if (my_push.type == TypePush.BAN)
        {
            bus_main_events.ps_to_logout.onNext(Any())
            return
        }

        if(SharedPrefsManager.pref_current_user.get().value == null)
        {
            return
        }

        val notification = getNotification(my_push)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            showHighNotification(notification)
        }
        else
        {
            showLowNotification(notification)
        }
    }

    fun getNotification(my_push: MyPush): Notification
    {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(AppClass.app, getStringMy(R.string.notify_chanel_akcsl))
                .setSmallIcon(R.drawable.ei_kei_logo)
                .setLargeIcon(BitmapFactory.decodeResource(AppClass.app.getResources(), R.drawable.ei_kei_logo))
                .setContentTitle("Обновление")
                .setContentText(my_push.getTitle())
                .setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
                .setPriority(Notification.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(getIntentFromPush(my_push))
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setSound(sound)

        return builder.build()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun showHighNotification(notification: Notification)
    {
        val notificationChannel = NotificationChannel(getStringMy(R.string.notify_chanel_akcsl), getStringMy(R.string.notify_chanel_akcsl), NotificationManager.IMPORTANCE_HIGH)

        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val att = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

        notificationChannel.description = getStringMy(R.string.notify_chanel_akcsl)
        notificationChannel.enableLights(true)
        notificationChannel.enableVibration(true)
        notificationChannel.setSound(sound, att)
        notificationChannel.lightColor = Color.RED
        notificationChannel.setShowBadge(false)

        val notificationManager = AppClass.app.getSystemService(android.app.NotificationManager::class.java)
        notificationManager?.createNotificationChannel(notificationChannel)
        notificationManager?.notify((0..999).random(), notification)
    }

    private fun showLowNotification(notification: Notification)
    {
        val notificationManagerCompat = NotificationManagerCompat.from(AppClass.app)
        notificationManagerCompat.notify((0..999).random(), notification)
    }


    fun getIntentFromPush(my_push: MyPush): PendingIntent
    {
        val intent = Intent(AppClass.app, ActMain::class.java)
        intent.makeClearAllPrevious()
        intent.putExtra(Constants.Extras.MY_PUSH, my_push)

        val pendingIntent = PendingIntent.getActivity(AppClass.app, (0..999).random(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return pendingIntent
    }
}