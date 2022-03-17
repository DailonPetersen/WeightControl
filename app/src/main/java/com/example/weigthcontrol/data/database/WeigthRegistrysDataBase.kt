package com.example.weigthcontrol.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weigthcontrol.data.Converters
import com.example.weigthcontrol.data.dao.ExerciseDao
import com.example.weigthcontrol.data.model.Registry
import com.example.weigthcontrol.data.dao.RegistryDao
import com.example.weigthcontrol.data.model.Exercise

@Database(entities = [Registry::class, Exercise::class], version = 1, exportSchema = false)
abstract class WeigthRegistrysDataBase : RoomDatabase() {

    abstract fun registryDao(): RegistryDao
    abstract fun exerciseDao(): ExerciseDao

    companion object {
        @Volatile
        private var INSTANCE: WeigthRegistrysDataBase? = null

        fun getInstanceDatabase(context: Context): WeigthRegistrysDataBase {
            return INSTANCE ?: synchronized(lock = Any()){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeigthRegistrysDataBase::class.java,
                    "weigth_registry_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

