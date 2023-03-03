package com.emmanuelamet.dogs.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emmanuelamet.dogs.db.DogDatabase
import com.emmanuelamet.dogs.model.DogBreed
import com.emmanuelamet.dogs.nofication.NotificationHelper
import com.emmanuelamet.dogs.pref.SharedPreferencesHelper
import com.emmanuelamet.dogs.servvice.DogApiService
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : BaseViewModel(application) {

    private var refreshTime  = 5 * 60 * 1000 * 1000 * 1000L
    private val prefHelper = SharedPreferencesHelper(getApplication())
    private val dogService = DogApiService()
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogLoadError = MutableLiveData<Boolean>()
    val loading  = MutableLiveData<Boolean>()

    fun refresh(){
        checkCacheDuration()
        val updateTime  = prefHelper.getUpdateTime()
        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime){
            fetchFromDatabase()
        }else{
            fetchFromRemote()
        }

    }

    private fun checkCacheDuration(){
        val cachePreference = prefHelper.getCacheDuration()
        try {
            val cachePreferenceInt = cachePreference?.toInt() ?: (5 * 60)
            refreshTime = cachePreferenceInt.times(60 * 1000 * 1000 * 1000L)
        }catch (e: java.lang.NumberFormatException){
            e.printStackTrace()
        }
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
                        Toast.makeText(getApplication(), "Dogs retrieved from endpoint.", Toast.LENGTH_LONG).show()
                    }

                    override fun onError(error: Throwable) {
                        dogLoadError.value = true
                        loading.value = false
                        error.printStackTrace()
                    }

                })
        )
    }

    private fun fetchFromDatabase(){
        loading.value = true
        launch {
            val dogs =  DogDatabase(getApplication())
                .dogDao()
                .getAllDogs()
            dogsRetrieve(dogs)
            Toast.makeText(getApplication(), "Dogs retrieved from local database", Toast.LENGTH_LONG).show()
            NotificationHelper(getApplication()).createNotification()
        }
    }

    fun refreshByPassCache(){
        fetchFromRemote()
    }

    private fun dogsRetrieve(dogList : List<DogBreed>){
        dogs.value = dogList
        loading.value = false
        dogLoadError.value = false
    }


    private fun storeDogsLocally(list: List<DogBreed>){
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            dao.deleteAllDogs()
            val result = dao.insertAll(*list.toTypedArray())
            var i = 0
            while(i < list.size){
                list[i].uuid = result[i].toInt()
                ++i
            }
            dogsRetrieve(list)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}