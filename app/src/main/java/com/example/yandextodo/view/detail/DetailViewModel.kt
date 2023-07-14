package com.example.yandextodo.view.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yandextodo.data.Model
import com.example.yandextodo.data.TodoRepository
import com.example.yandextodo.data.model.Priority
import com.example.yandextodo.data.model.TodoEntity

import kotlinx.coroutines.launch

class DetailViewModel(private val repository: TodoRepository) : ViewModel() {

    fun addTodo(todo: Model) = viewModelScope.launch {
        repository.addTodo(todo)
    }
    fun updateTodo(todo: Model) = viewModelScope.launch {
        repository.updateTodo(todo)
    }

    fun deleteTodo(todo: Model) = viewModelScope.launch {
        repository.deleteTodo(todo)
    }
    private var _isOnUpdatingTodo = false

    val isOnUpdatingTodo get() = _isOnUpdatingTodo

    fun setIsOnUpdatingTodo(state: Boolean) {
        _isOnUpdatingTodo = state
    }


    fun createTodoEntity(description: String): TodoEntity {
        // Create and return the TodoEntity instance based on the input data
        return TodoEntity(
            id = 0, // Assign the appropriate ID
            description = description,
            priority = Priority.BASIC, // Assign the appropriate priority
            deadline = null, // Assign the appropriate deadline
            flag = false, // Assign the appropriate flag
            createdAt = System.currentTimeMillis(), // Assign the appropriate created timestamp
            changedAt = System.currentTimeMillis(), // Assign the appropriate changed timestamp
            updatedAt = System.currentTimeMillis(), // Assign the appropriate updated timestamp
            lastUpdatedBy = "user", // Assign the appropriate last updated by value
            color = null // Assign the appropriate color
        )
    }
}