package com.emmanuelamet.dogs.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class DogBreed(
    @ColumnInfo(name = "breed_id")
    @SerializedName("id")
    val BreedId: String?,

    @ColumnInfo(name = "dog_name")
    @SerializedName("name")
    val dogBreed: String?,

    @ColumnInfo(name = "life_span")
    @SerializedName("life_span")
    val lifeSpan: String?,

    @ColumnInfo(name = "breed_group")
    @SerializedName("breed_group")
    val breedGroup: String?,

    @ColumnInfo(name = "bread_for")
    @SerializedName("bred_for")
    val breedFor: String?,

    val temperament: String?,

    @ColumnInfo(name = "dog_url")
    @SerializedName("url")
    val imageUrl: String?,

    val origin: String?
){
    @PrimaryKey(autoGenerate = true)
    var uuid : Int = 0
}

data class DogPalette(var color: Int)