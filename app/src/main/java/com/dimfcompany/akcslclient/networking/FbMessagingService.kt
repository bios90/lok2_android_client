package com.dimfcompany.akcslclient.networking

import android.util.Log
import com.dimfcompany.akcslclient.base.AppClass
import com.dimfcompany.akcslclient.base.BusMainEvents
import com.dimfcompany.akcslclient.base.enums.TypePush
import com.dimfcompany.akcslclient.base.extensions.asOptional
import com.dimfcompany.akcslclient.base.extensions.getString
import com.dimfcompany.akcslclient.logic.SharedPrefsManager
import com.dimfcompany.akcslclient.logic.utils.NotificationManager
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject

class FbMessagingService : FirebaseMessagingService()
{
    @Inject
    lateinit var notofication_manger: NotificationManager

    init
    {
        AppClass.app_component.inject(this)
    }

    override fun onNewToken(new_token: String)
    {
        super.onNewToken(new_token)
        SharedPrefsManager.pref_fb_token.asConsumer().accept(new_token.asOptional())
        subscribeToTopic()
    }

    override fun onMessageReceived(remote_message: RemoteMessage)
    {
        super.onMessageReceived(remote_message)
        notofication_manger.notify(remote_message)
    }

    private fun subscribeToTopic()
    {
        FirebaseMessaging.getInstance().subscribeToTopic("documents")
                .addOnSuccessListener({})
    }
}