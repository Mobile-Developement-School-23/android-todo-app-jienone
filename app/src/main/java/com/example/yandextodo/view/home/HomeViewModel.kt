package com.example.yandextodo.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yandextodo.core.vo.LoadResult
import com.example.yandextodo.data.Model
import com.example.yandextodo.data.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: TodoRepository) : ViewModel() {


    private val _todos: MutableStateFlow<LoadResult<List<Model>>> =
        MutableStateFlow(LoadResult.Loading())

    val todos: StateFlow<LoadResult<List<Model>>>
        get() = _todos

    private val checkboxStates: MutableMap<Int, Boolean> = mutableMapOf()



    fun getAllTodos() = viewModelScope.launch {
        try {
            repository.getAllTodos().collect { list ->
                _todos.value = list
            }
        } catch (e: Exception) {
            _todos.value = LoadResult.Error(message = e.message.toString())
        }
    }
    fun setCheckboxState(itemId: Int, isChecked: Boolean) {
        checkboxStates[itemId] = isChecked
    }

    fun getCheckboxState(itemId: Int): Boolean {
        return checkboxStates[itemId] ?: false
    }
    fun deleteTodo(position: Int) = viewModelScope.launch {
        val todos = _todos.value
        if (todos is LoadResult.Success) {
            val todoList = todos.data
            if (todoList?.indices?.contains(position) == true) {
                val todo = todoList.getOrNull(position)
                todo?.let { repository.deleteTodo(it) }
            }
        }
    }



}