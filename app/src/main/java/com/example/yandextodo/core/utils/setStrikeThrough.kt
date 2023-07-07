package com.example.yandextodo.core.utils

import android.graphics.Paint
import android.widget.TextView

fun TextView.setStrikeThrough(strikethrough: Boolean) {
    paintFlags = if (strikethrough) {
        paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}