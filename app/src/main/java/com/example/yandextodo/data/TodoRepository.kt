package com.example.yandextodo.data

import android.content.ClipData.Item
import android.util.Log
import com.example.yandextodo.core.di.Injection
import com.example.yandextodo.core.utils.DataMapper
import com.example.yandextodo.core.utils.convertTimestampToDate
import com.example.yandextodo.core.vo.LoadResult
import com.example.yandextodo.data.network.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class TodoRepository(
    private val todoLocalDataSource: TodoLocalDataSource,
    private val dispatcher: CoroutineDispatcher
) {

    private var currentRevision: Int = 0

    private val apiService: ApiService = Injection.provideApiService()
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
            try {
                val response = apiService.getAllTodos()
                val remoteTodos = response.list

                currentRevision = response.revision
                try {
                    val todoEntities = mutableListOf<TodoEntity>()
                    for (remoteTodo in remoteTodos) {
                        try {

                            val todoEntity = DataMapper.mapTodoDomainToEntity(remoteTodo)
                            todoEntity.deadline = convertTimestampToDate(remoteTodo.deadline)
                            todoEntities.add(todoEntity)
                            todoLocalDataSource.addAllTodos(todoEntities)
                        } catch (e: NumberFormatException) {
                            Log.e("INVALID_ID", "Skipping element with invalid ID: ${remoteTodo.id}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("HZ_WHY_NOT_ADDED", e.toString())
                }
            } catch (e: Exception) {
                Log.e("NOT_RECEIVED", "Something went wrong while receiving")
                Log.e("NOT_RECEIVED", e.toString())
            }

            flow {
                val localTodos = todoLocalDataSource.getAllTodos().first()
                println("LOCAL_TODOS $localTodos")
                val mappedTodos = DataMapper.mapTodoEntitiesToDomain(localTodos)
                println("MAPPED_TODOS $mappedTodos")
                emit(LoadResult.Success(mappedTodos))
            }
        }
    }





    suspend fun addTodo(item: Model) {
        withContext(dispatcher) {
            val response = apiService.addItem(currentRevision, ItemContainer(item))
            if (response.isSuccessful) {
                todoLocalDataSource.addTodoToDatabase(DataMapper.mapTodoDomainToEntity(item))
            } else {
                // Handle the error response
                Log.d("HTTP_RESPONSE", response.code().toString())
                // Perform error handling logic here
            }
        }
    }


    suspend fun updateTodo(item: Model) {
        withContext(dispatcher) {
            val response = apiService.updateItem(currentRevision, item.id, ItemContainer(item))
            Log.d("RESPONSE_UPDATE", response.toString())
            todoLocalDataSource.updateTodo(DataMapper.mapTodoDomainToEntity(item))
        }
    }

    suspend fun deleteTodo(todo: Model) {
        withContext(dispatcher) {
            apiService.deleteItem(currentRevision, todo.id)
            todoLocalDataSource.deleteTodoFromDatabase(DataMapper.mapTodoDomainToEntity(todo))
        }
    }

    suspend fun markAsDone(todo: Model) {
        withContext(dispatcher) {
            todoLocalDataSource.markAsDone(DataMapper.mapTodoDomainToEntity(todo))
            apiService.updateItem(currentRevision, todo.id, ItemContainer(todo))
        }
    }
    suspend fun markAsNotDone(todo: Model) {
        withContext(dispatcher) {
            todoLocalDataSource.markAsNotDone(DataMapper.mapTodoDomainToEntity(todo))
            apiService.updateItem(currentRevision, todo.id, ItemContainer(todo))
        }
    }

    suspend fun countElementsWithProperty(): Int {
        return withContext(dispatcher) {
            return@withContext todoLocalDataSource.countElementsWithProperty()
        }
    }

}