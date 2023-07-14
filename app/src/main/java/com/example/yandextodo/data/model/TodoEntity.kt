    package com.example.yandextodo.data.model

    import android.os.Parcelable
    import androidx.room.Entity
    import androidx.room.PrimaryKey
    import com.google.gson.annotations.SerializedName
    import kotlinx.parcelize.Parcelize

    @Entity(tableName = "todo_data")
    data class TodoEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val description: String,
        val priority: Priority,
        var deadline: String?,
        val flag: Boolean,
        val createdAt: Long,
        val changedAt: Long,
        val updatedAt: Long,
        val lastUpdatedBy: String,
        val color: String?
    )


    data class ItemContainer(
        val element: Model
    )

    data class ListResponse(
        val list: List<Model>,
        val revision: Int,
        val status: String
    )

    @Parcelize
    data class Model(
        val id: String,

        @SerializedName("text")
        val description: String,

        @SerializedName("importance")
        val priority: Priority,

        var deadline: Long?,

        @SerializedName("done")
        var flag: Boolean,

        @SerializedName("created_at")
        val createdAt: Long,
        @SerializedName("changed_at")
        val changedAt: Long,

        @SerializedName("color")
        val color: String? = null,

        @SerializedName("updated_at")
        val updatedAt: Long,

        @SerializedName("last_updated_by")
        val lastUpdatedBy: String

    ) : Parcelable

    enum class Priority {
        @SerializedName("low")
        LOW,
        @SerializedName("basic")
        BASIC,
        @SerializedName("important")
        IMPORTANT,
    }