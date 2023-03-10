package com.emmanuelamet.dogs.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.emmanuelamet.dogs.R
import com.emmanuelamet.dogs.model.DogBreed

@Dao
interface DogDao {
    @Insert
    suspend fun insertAll(vararg dogBreed: DogBreed) : List<Long>

    @Query("SELECT * FROM dogbreed")
    suspend fun getAllDogs() : List<DogBreed>

    @Query("SELECT * FROM dogbreed WHERE uuid = :dogId")
    suspend fun getDog(dogId: Int) : DogBreed

    @Query("DELETE FROM dogbreed")
    suspend fun deleteAllDogs()
}