package com.example.yandextodo.core.utils

import com.example.yandextodo.data.Model
import com.example.yandextodo.data.TodoEntity

/**
 * This class is used for mapping the to-do data class. The intent of changing to-do data class
 * whenever moving from between layer is to making the code more easy to maintained and
 * become more robust
 *
 */
object DataMapper {

    fun mapTodoEntitiesToDomain(list: List<TodoEntity>): List<Model> {
        val newList = ArrayList<Model>()

        list.map { todoEntity ->
            val todo = Model(
                id = todoEntity.id,
                description = todoEntity.description,
                createdAt = todoEntity.createdAt,
                updatedAt = todoEntity.updatedAt,
                deadline = todoEntity.deadline,
                priority = todoEntity.priority,
                flag = false
            )
            newList.add(todo)
        }

        return newList
    }


    fun mapTodoDomainToEntity(todo: Model): TodoEntity {
        return TodoEntity(
            id = todo.id,
            description = todo.description,
            createdAt = todo.createdAt,
            updatedAt = todo.updatedAt,
            deadline = todo.deadline,
            priority = todo.priority,
            flag = false
        )
    }

}