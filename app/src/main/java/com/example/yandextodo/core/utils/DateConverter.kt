package com.example.yandextodo.core.utils

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date


fun convertDateToTimestamp(dateString: String?): Long? {
        if (dateString.isNullOrEmpty()) {
            return null
        }

        val format = SimpleDateFormat("dd.MM.yyyy")
        return try {
            val date = format.parse(dateString)
            date.time
        } catch (e: Exception) {
            null
        }
    }

    // Функция для преобразования таймстампа в дату в формате "dd.MM.yyyy"
    fun convertTimestampToDate(timestamp: Long?): String? {
        if (timestamp == null) {
            return null
        }

        return try {
            val date = Date(timestamp)
            val format = SimpleDateFormat("dd.MM.yyyy")
            format.format(date)
        } catch (e: Exception) {
            null
        }
    }