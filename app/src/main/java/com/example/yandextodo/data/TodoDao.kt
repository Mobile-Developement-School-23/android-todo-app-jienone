package com.example.yandextodo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface TodoDao {


    @Query("SELECT COUNT(*) FROM todo_data WHERE flag = 1")
    suspend fun countElementsWithProperty(): Int
    @Query("SELECT * FROM todo_data ORDER BY updatedAt DESC")
    fun getAllTodos(): Flow<List<TodoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodo(todoEntity: TodoEntity)

    @Delete
    suspend fun deleteTodo(todoEntity: TodoEntity)

    @Update
    suspend fun updateTodo(todoEntity: TodoEntity)

    @Update
    suspend fun markAsDone(todoEntity: TodoEntity)
    @Update
    suspend fun markAsNotDone(todoEntity: TodoEntity)
}

