package com.emmanuelamet.dogs.servvice

import com.emmanuelamet.dogs.model.DogBreed
import io.reactivex.Single
import retrofit2.http.GET

interface DogApi {
    @GET("DevTides/DogsApi/master/dogs.json")
    fun getDogs() : Single<List<DogBreed>>
}