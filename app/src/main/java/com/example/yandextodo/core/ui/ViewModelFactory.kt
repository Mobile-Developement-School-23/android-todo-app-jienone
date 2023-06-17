package com.example.yandextodo.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yandextodo.view.detail.DetailViewModel
import com.example.yandextodo.data.TodoRepository
import com.example.yandextodo.view.home.HomeViewModel


class ViewModelFactory(private val todoRepository: TodoRepository) : ViewModelProvider.Factory {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(todoRepository: TodoRepository): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(todoRepository).apply { instance = this }
            }
    }


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(todoRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(todoRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown View model class")
        }
    }
}