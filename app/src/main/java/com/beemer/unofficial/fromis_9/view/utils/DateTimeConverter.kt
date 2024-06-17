package com.beemer.unofficial.fromis_9.view.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateTimeConverter {
    fun dateTimeToString(date: String, originalFormat: String, targetFormat: String, locale: Locale): String {
        val originalFormatter = DateTimeFormatter.ofPattern(originalFormat, locale)
        val targetFormatter = DateTimeFormatter.ofPattern(targetFormat, locale)
        val dateTime = LocalDateTime.parse(date, originalFormatter)
        return dateTime.format(targetFormatter)
    }

    fun stringToDateTime(dateTime: String, format: String, locale: Locale): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern(format, locale)
        return LocalDateTime.parse(dateTime, formatter)
    }

    fun stringToDate(date: String, format: String, locale: Locale): LocalDate {
        val formatter = DateTimeFormatter.ofPattern(format, locale)
        return LocalDate.parse(date, formatter)
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

    fun timeAgo(dateTime: String): String {
        val now = LocalDateTime.now()
        val past = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME)
        val diff = now.toEpochSecond(ZoneOffset.of("+09:00")) - past.toEpochSecond(ZoneOffset.of("+09:00"))

        return when {
            diff < 60 -> "방금 전"
            diff < 3600 -> "${diff / 60}분 전"
            diff < 86400 -> "${diff / 3600}시간 전"
            diff < 604800 -> "${diff / 86400}일 전"
            diff < 2419200 -> "${diff / 604800}주 전"
            else -> past.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        }
    }
}