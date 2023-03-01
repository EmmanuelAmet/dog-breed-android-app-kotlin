package com.emmanuelamet.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emmanuelamet.dogs.model.DogBreed

class DetailViewModel : ViewModel(){
    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch(){

    }
}