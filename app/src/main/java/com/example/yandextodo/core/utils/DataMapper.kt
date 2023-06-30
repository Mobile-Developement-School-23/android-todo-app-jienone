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
        return list.map {
            Model(
                id = it.id.toString(),
                deadline = convertDateToTimestamp(it.deadline),
                description = it.description,
                lastUpdatedBy = it.lastUpdatedBy,
                flag = it.flag,
                createdAt = it.createdAt,
                changedAt = it.changedAt,
                updatedAt = it.updatedAt,
                priority = it.priority,
                color = it.color
            )
        }
    }


    fun mapTodoDomainToEntity(todo: Model): TodoEntity {
        return TodoEntity(
            id = todo.id.toLong(),
            description = todo.description,
            createdAt = todo.createdAt,
            changedAt = todo.changedAt,
            updatedAt = todo.updatedAt,
            deadline = convertTimestampToDate(todo.deadline),
            priority = todo.priority,
            flag = todo.flag,
            lastUpdatedBy = todo.lastUpdatedBy,
            color = todo.color
        )
    }

}