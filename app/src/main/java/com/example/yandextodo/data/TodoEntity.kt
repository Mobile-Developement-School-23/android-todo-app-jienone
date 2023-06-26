package com.example.yandextodo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_data")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    val priority: String,
    val deadline: String,
    val flag: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val lastUpdatedBy: String
)