package com.example.yandextodo.data.network

import android.content.ClipData.Item
import com.example.yandextodo.data.ItemContainer
import com.example.yandextodo.data.ListResponse
import com.example.yandextodo.data.Model
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

const val BASE_URL: String = "https://beta.mrdekk.ru/todobackend/"

interface ApiService {
    @GET("list")
    suspend fun getAllTodos(): ListResponse

    @GET("list/{id}")
    suspend fun getItemById(@Path("id") id: String): Model

    @POST("list")
    suspend fun addItem(@Header("X-Last-Known-Revision") revision: Int, @Body item: ItemContainer) : Response<ItemContainer>

    @PUT("list/{id}")
    suspend fun updateItem(@Header("X-Last-Known-Revision") revision: Int, @Path("id") id: String, @Body item: ItemContainer): Response<ItemContainer>

    @DELETE("list/{id}")
    suspend fun deleteItem(@Header("X-Last-Known-Revision") revision: Int, @Path("id") id: String): Response<ItemContainer>
}
