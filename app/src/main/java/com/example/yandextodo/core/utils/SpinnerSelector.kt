package com.example.yandextodo.core.utils

import com.example.yandextodo.data.Priority

internal fun selectedSpinnerItem(choosedPrio: String): Priority {
    return when (choosedPrio) {
        "Низкая" -> Priority.LOW
        "Нет" -> Priority.BASIC
        "Высокая" -> Priority.IMPORTANT
        else -> Priority.BASIC
    }
}
