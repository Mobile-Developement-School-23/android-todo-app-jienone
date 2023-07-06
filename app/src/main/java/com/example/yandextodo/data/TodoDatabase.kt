package com.example.yandextodo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [TodoEntity::class], version = 10, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun getTodoDao(): TodoDao

    companion object {
        @Volatile
        private var instance: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).apply { instance = this }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, TodoDatabase::class.java, "room_db")
                .fallbackToDestructiveMigration()
                .build()

    }

}
