package com.example.randomzooanimals

import retrofit2.Response
import retrofit2.http.GET

interface ZooAnimalsApi {
    @GET("/animals/rand")
    suspend fun getAnimal(): Response<ZooAnimalResponse>
}