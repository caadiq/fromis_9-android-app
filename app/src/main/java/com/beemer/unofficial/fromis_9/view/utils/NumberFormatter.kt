package com.beemer.unofficial.fromis_9.view.utils

import java.text.NumberFormat
import java.util.Locale

object NumberFormatter {
    fun formatViews(views: Int): String {
        return when {
            views < 100000 -> "${NumberFormat.getNumberInstance(Locale.US).format(views)}회"
            else -> "${NumberFormat.getNumberInstance(Locale.US).format(views / 10000)}만 회"
        }
    }

    fun formatNumber(number: Int): String {
        return NumberFormat.getNumberInstance(Locale.US).format(number)
    }
}