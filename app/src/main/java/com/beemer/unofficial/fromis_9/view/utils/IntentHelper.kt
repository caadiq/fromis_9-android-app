package com.beemer.unofficial.fromis_9.view.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri

object IntentHelper {
    fun openUri(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())

        val packageManager = context.packageManager
        val activities = packageManager.queryIntentActivities(intent, 0)
        val isIntentSafe = activities.isNotEmpty()

        if (isIntentSafe)
            context.startActivity(intent)
        else
            Toast.makeText(context, "해당 URL을 열 수 있는 앱이 없습니다.", Toast.LENGTH_SHORT).show()
    }
}