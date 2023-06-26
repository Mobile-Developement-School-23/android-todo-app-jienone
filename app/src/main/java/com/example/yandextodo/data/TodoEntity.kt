package com.example.yandextodo.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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
) {
    fun toDomain() = Model(
        id = this.id.toString(),
        description = this.deadline,
        priority = this.priority,
        this.deadline,
        this.flag,
        this.createdAt,
        this.updatedAt,
        this.lastUpdatedBy
    )
}
@Parcelize
data class Model(
    val id: String = "0",

    @SerializedName("text")
    val description: String,

    @SerializedName("importance")
    val priority: String,

    val deadline: String,

    @SerializedName("done")
    var flag: Boolean,

    @SerializedName("created_at")
    val createdAt: Long,

    @SerializedName("updated_at")
    val updatedAt: Long,

    @SerializedName("last_updated_by")
    val lastUpdatedBy: String

) : Parcelable
