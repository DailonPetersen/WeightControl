package com.example.weigthcontrol.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weigthcontrol.data.dao.RegistryDao
import com.example.weigthcontrol.data.database.WeigthRegistrysDataBase
import com.example.weigthcontrol.data.model.Registry
import kotlinx.coroutines.*

class RegistryRepo() {

    companion object {

        private lateinit var INSTANCE: WeigthRegistrysDataBase
        private var list: List<Registry>? = null
        private fun initializeDatabaseInstance(context: Context) {
            INSTANCE = WeigthRegistrysDataBase.getInstanceDatabase(context)
        }

        fun insertRegistry(context: Context, registry: Registry) {
            initializeDatabaseInstance(context)
            CoroutineScope(Dispatchers.IO).launch {
                INSTANCE.registryDao().addWeightRegistry(registry)
            }
        }

        fun deleteRegistry(context: Context, registry: Registry) {
            initializeDatabaseInstance(context)
            CoroutineScope(Dispatchers.IO).launch {
                INSTANCE.registryDao().deleteRegistry(registry)
            }
        }

        fun getRegistriesByExercise(context: Context, exerciseId: Int): List<Registry>? {
            initializeDatabaseInstance(context)
            runBlocking {
                list = INSTANCE.registryDao().getRegistriesByExercise(exerciseId)
            }
            return if (!list.isNullOrEmpty()){
                list
            } else {
                null
            }
        }

        fun getAll(context: Context): List<Registry>? {
            initializeDatabaseInstance(context)
            CoroutineScope(Dispatchers.IO).launch {
                list = INSTANCE.registryDao().getAllRegistry()
            }
            return if (list != null){
                list
            } else {
                null
            }
        }
    }
}