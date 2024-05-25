package com.beemer.unofficial.fromis_9.view.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateTimeConverter {
    fun dateTimeToString(date: String, originalFormat: String, targetFormat: String, locale: Locale): String {
        val originalFormatter = DateTimeFormatter.ofPattern(originalFormat, locale)
        val targetFormatter = DateTimeFormatter.ofPattern(targetFormat, locale)
        val dateTime = LocalDateTime.parse(date, originalFormatter)
        return dateTime.format(targetFormatter)
    }

    fun secToTime(sec: Int): String {
        val hour = sec / 3600
        val minute = sec % 3600 / 60
        val second = sec % 60

        return if (hour > 0) {
            String.format(Locale.KOREA, "%d:%02d:%02d", hour, minute, second)
        } else {
            String.format(Locale.KOREA, "%d:%02d", minute, second)
        }
    }
}