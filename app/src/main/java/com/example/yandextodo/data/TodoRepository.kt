package com.example.yandextodo.data

import com.example.yandextodo.core.vo.LoadResult
import com.example.yandextodo.core.utils.DataMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TodoRepository(
    private val todoLocalDataSource: TodoLocalDataSource,
    private val dispatcher: CoroutineDispatcher
) {

    companion object {
        @Volatile
        private var instance: TodoRepository? = null

        fun getInstance(
            todoLocalDataSource: TodoLocalDataSource,
            dispatcher: CoroutineDispatcher = Dispatchers.Default
        ): TodoRepository =
            instance ?: synchronized(this) {
                instance ?: TodoRepository(todoLocalDataSource, dispatcher).apply {
                    instance = this
                }
            }

    }

    suspend fun getAllTodos(): Flow<LoadResult<List<Model>>> {
        return withContext(dispatcher) {
            return@withContext todoLocalDataSource.getAllTodos().map {
                LoadResult.Success(DataMapper.mapTodoEntitiesToDomain(it))
            }
        }
    }

    suspend fun addTodo(todo: Model) {
        withContext(dispatcher) {
            todoLocalDataSource.addTodoToDatabase(DataMapper.mapTodoDomainToEntity(todo))
        }
    }

    suspend fun updateTodo(todo: Model) {
        withContext(dispatcher) {
            todoLocalDataSource.updateTodo(DataMapper.mapTodoDomainToEntity(todo))
        }
    }

    suspend fun deleteTodo(todo: Model) {
        withContext(dispatcher) {
            todoLocalDataSource.deleteTodoFromDatabase(DataMapper.mapTodoDomainToEntity(todo))
        }
    }


}