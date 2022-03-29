package com.example.randomzooanimals.main

import com.example.randomzooanimals.ZooAnimalResponse
import com.example.randomzooanimals.util.Resource

//i used an interface so when we need to test, we can make a repository that makes network calls and
//one that doesnt
interface MainRepository {

    suspend fun getZooAnimal(): Resource<ZooAnimalResponse>
}