package com.example.yandextodo.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yandextodo.data.Model
import com.example.yandextodo.data.TodoRepository

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

    // This variable is used for determine condition, whether in creating to-do or updating to-do condition
    private var _isOnUpdatingTodo = false

    val isOnUpdatingTodo get() = _isOnUpdatingTodo

    fun setIsOnUpdatingTodo(state: Boolean) {
        _isOnUpdatingTodo = state
    }
}