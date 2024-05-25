package com.beemer.unofficial.fromis_9.view.utils

import java.text.NumberFormat
import java.util.Locale

object NumberFormatter {
    fun formatNumber(number: Int): String {
        return when {
            number < 100000 -> "${NumberFormat.getNumberInstance(Locale.US).format(number)}회"
            else -> "${NumberFormat.getNumberInstance(Locale.US).format(number / 10000)}만 회"
        }
    }
}