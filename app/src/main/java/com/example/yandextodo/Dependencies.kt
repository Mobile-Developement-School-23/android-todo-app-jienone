package com.example.yandextodo

import android.content.Context
import androidx.room.Room
import com.example.yandextodo.data.TodoDao
import com.example.yandextodo.data.TodoDatabase
import com.example.yandextodo.data.TodoRepository

object Dependencies {
    private lateinit var applicationContext: Context


    private const val SHARED_PREF_NAME = "RevisionPrefs"
    private const val KEY_REVISION = "revision"

    fun init(context: Context) {
        applicationContext = context
    }

    private val appDatabase: TodoDatabase by lazy {
        Room.databaseBuilder(applicationContext, TodoDatabase::class.java, "database.db")
            .createFromAsset("todo_database.db")
            .build()
    }

    fun saveRevision(revision: Int) {
        val sharedPreferences = applicationContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_REVISION, revision)
        editor.apply()
    }

    fun getRevision(): Int {
        val sharedPreferences = applicationContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_REVISION, 0)
    }
}