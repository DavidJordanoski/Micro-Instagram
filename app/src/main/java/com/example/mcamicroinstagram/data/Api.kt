package com.example.mcamicroinstagram.data

import com.example.mcamicroinstagram.model.Photos
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path


interface Api {
    @GET("/photos")
    fun getPhotos(): Call<List<Photos>>

    @PUT("/photos/{id}")
    fun putPhoto(@Path("id") id: Int,@Body photo: Photos): Call<Photos>

    @PATCH("/photos/{id}")
    fun patchPhoto(@Path("id") id: Int,@Body photo: Photos): Call<Photos>

    @DELETE("/photos/{id}")
    fun deletePhoto(@Path("id") id: Int): Call<Void>
}