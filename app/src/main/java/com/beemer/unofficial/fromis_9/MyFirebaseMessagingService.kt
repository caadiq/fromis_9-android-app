package com.beemer.unofficial.fromis_9

import android.annotation.SuppressLint
import android.provider.Settings
import com.beemer.unofficial.fromis_9.model.dto.FcmTokenDto
import com.beemer.unofficial.fromis_9.model.repository.FcmRepository
import com.beemer.unofficial.fromis_9.view.utils.Notification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var fcmRepository: FcmRepository

    private lateinit var notification: Notification
    private lateinit var ssaid: String

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        super.onCreate()
        notification = Notification(applicationContext)
    }

    @SuppressLint("HardwareIds")
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        coroutineScope.launch {
            try {
                ssaid = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                fcmRepository.sendFcmToken(FcmTokenDto(ssaid, token))
            } catch (_: Exception) { }
        }
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

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}