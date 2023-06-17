package com.example.yandextodo.core.utils

import android.content.Context
import android.widget.Toast

fun Context.showLongToastMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}