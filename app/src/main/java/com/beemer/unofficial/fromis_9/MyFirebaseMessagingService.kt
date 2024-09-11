package com.beemer.unofficial.fromis_9

import android.provider.Settings
import com.beemer.unofficial.fromis_9.view.utils.Notification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private lateinit var notification: Notification
    private val ssaid = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        
        // TODO: 서버에 토큰 전송
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        notification = Notification(applicationContext)

        remoteMessage.data.let { data ->
            val title = data["title"] ?: ""
            val body = data["body"] ?: ""

            notification.deliverNotification(title, body)
        }
    }
}