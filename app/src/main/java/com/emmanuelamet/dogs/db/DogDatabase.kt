package com.emmanuelamet.dogs.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emmanuelamet.dogs.R
import com.emmanuelamet.dogs.dao.DogDao
import com.emmanuelamet.dogs.model.DogBreed

@Database(entities = [DogBreed::class], version = 1)
abstract class DogDatabase : RoomDatabase(){
    abstract fun dogDao() : DogDao

    companion object{
        @Volatile private var instance: DogDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also{
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            DogDatabase::class.java,
            "${R.string.database_name}"
        ).build()
    }
}