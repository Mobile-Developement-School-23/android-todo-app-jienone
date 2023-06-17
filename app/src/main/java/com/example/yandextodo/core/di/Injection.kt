package com.example.yandextodo.core.di

import android.content.Context
import com.example.yandextodo.core.ui.ViewModelFactory
import com.example.yandextodo.data.TodoDatabase
import com.example.yandextodo.data.TodoLocalDataSource
import com.example.yandextodo.data.TodoRepository

import kotlinx.coroutines.Dispatchers

/**
 * This class is used for manual dependency injection.
 */
object Injection {

    private fun provideIODispatcher() = Dispatchers.IO

    private fun provideLocalDatabase(context: Context) = TodoDatabase.getInstance(context)

    private fun provideTodoLocalDataSource(context: Context) =
        TodoLocalDataSource.getInstance(
            provideLocalDatabase(context).getTodoDao(),
            provideIODispatcher()
        )

    private fun provideTodoRepository(context: Context) =
        TodoRepository.getInstance(provideTodoLocalDataSource(context), provideIODispatcher())

    fun provideViewModelFactory(context: Context) =
        ViewModelFactory.getInstance(provideTodoRepository(context))
}