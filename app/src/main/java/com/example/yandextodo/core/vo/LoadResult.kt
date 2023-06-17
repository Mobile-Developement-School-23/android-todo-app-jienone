package com.example.yandextodo.core.vo

/**
 * This class is used as value object. It is used to tracking the state, whether on error, on loading,
 * or on success while retrieving the data so that we can notify the user
 */
sealed class LoadResult<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : LoadResult<T>(data)
    class Loading<T>(data: T? = null) : LoadResult<T>(data)
    class Error<T>(message: String, data: T? = null) : LoadResult<T>(data, message)
}