package com.example.yandextodo.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TodoLocalDataSource(
    private val todoDao: TodoDao,
    private val dispatcher: CoroutineDispatcher
) {

    companion object {
        @Volatile
        private var instance: TodoLocalDataSource? = null

        fun getInstance(
            todoDao: TodoDao,
            dispatcher: CoroutineDispatcher = Dispatchers.Default
        ): TodoLocalDataSource =
            instance ?: synchronized(this) {
                instance ?: TodoLocalDataSource(todoDao, dispatcher).apply { instance = this }
            }
    }

    suspend fun getAllTodos(): Flow<List<TodoEntity>> {
        return withContext(dispatcher) {
            return@withContext todoDao.getAllTodos()
        }
    }

    suspend fun addTodoToDatabase(todoEntity: TodoEntity) {
        withContext(dispatcher) {
            todoDao.addTodo(todoEntity)
        }
    }
    suspend fun countElementsWithProperty(): Int {
        return withContext(dispatcher) {
            return@withContext todoDao.countElementsWithProperty()
        }
    }

    suspend fun deleteTodoFromDatabase(todoEntity: TodoEntity) {
        withContext(dispatcher) {
            todoDao.deleteTodo(todoEntity)
        }
    }

    suspend fun updateTodo(todoEntity: TodoEntity) {
        withContext(dispatcher) {
            todoDao.updateTodo(todoEntity)
        }
    }
    suspend fun markAsDone(todoEntity: TodoEntity) {
        withContext(dispatcher) {
            todoDao.markAsDone(todoEntity)
        }
    }
    suspend fun markAsNotDone(todoEntity: TodoEntity) {
        withContext(dispatcher) {
            todoDao.markAsNotDone(todoEntity)
        }
    }


}