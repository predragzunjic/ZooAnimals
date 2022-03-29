package com.example.randomzooanimals.main

import com.example.randomzooanimals.ZooAnimalResponse
import com.example.randomzooanimals.ZooAnimalsApi
import com.example.randomzooanimals.util.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: ZooAnimalsApi
): MainRepository {
    override suspend fun getZooAnimal(): Resource<ZooAnimalResponse> {
        return try{
            val response = api.getAnimal()
            val result = response.body()

            if(response.isSuccessful && result != null){
                Resource.Success(result)
            } else{
                Resource.Error(response.message())
            }
        }
        catch (e: Exception){
            //if its null, then "an error occurred"
            Resource.Error(e.message ?: "An error occurred")
        }

    }

}