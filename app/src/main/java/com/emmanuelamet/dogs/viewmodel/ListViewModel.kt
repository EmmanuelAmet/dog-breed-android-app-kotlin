package com.emmanuelamet.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emmanuelamet.dogs.model.DogBreed
import com.emmanuelamet.dogs.servvice.DogApiService
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel : ViewModel() {
    private val dogService = DogApiService()
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogLoadError = MutableLiveData<Boolean>()
    val loading  = MutableLiveData<Boolean>()

    fun refresh(){
        fetchFromRemote()
    }

    private fun fetchFromRemote(){
        loading.value = true
        disposable.add(
            dogService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>(){
                    override fun onSuccess(dogList: List<DogBreed>) {
                        storeDogsLocally(dogList)
                    }

                    override fun onError(error: Throwable) {
                        dogLoadError.value = true
                        loading.value = false
                        error.printStackTrace()
                    }

                })
        )
    }

    private fun dogsRetrieve(dogList : List<DogBreed>){
        dogs.value = dogList
        loading.value = false
        dogLoadError.value = false
    }

    private fun storeDogsLocally(dogList: List<DogBreed>){

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}