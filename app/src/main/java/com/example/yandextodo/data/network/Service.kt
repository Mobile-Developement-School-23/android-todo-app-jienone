package com.example.yandextodo.data.network

import com.example.yandextodo.data.Model
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

const val BASE_URL: String = "https://beta.mrdekk.ru/todobackend/"

interface ApiService {
    @GET("list")
    suspend fun getAllTodos(): Model

    @GET("list/{id}")
    suspend fun getItemById(@Path("id") id: String): Model

    @Headers("X-Last-Known-Revision: 0")
    @POST("list")
    suspend fun addItem(@Body item: Model): Model

    @POST("list")
    suspend fun updateItem(@Body item: Model): Model

    @POST("list")
    suspend fun deleteItem(@Body item: Model): Model
}
