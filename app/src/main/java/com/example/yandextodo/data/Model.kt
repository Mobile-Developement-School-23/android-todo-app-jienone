package com.example.yandextodo.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Model(
    val id: Int = 0,
    val description: String,
    val priority: String,
    val deadline: String,
    var flag: Boolean,
    val createdAt: Long,
    val updatedAt: Long
) : Parcelable
