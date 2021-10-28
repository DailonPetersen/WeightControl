package com.example.weigthcontrol.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weigthcontrol.data.dao.RegistryDao
import com.example.weigthcontrol.data.database.WeigthRegistrysDataBase
import com.example.weigthcontrol.data.model.Registry
import kotlinx.coroutines.*

class RegistryRepo() {
    companion object {
        private fun initializeDatabaseInstance(context: Context): WeigthRegistrysDataBase {
            return WeigthRegistrysDataBase.getInstanceDatabase(context)
        }

        private var lista: List<Registry>? = null

        fun insertRegistry(context: Context, regitry: Registry) {
            CoroutineScope(Dispatchers.IO).launch {
                val dataBase = initializeDatabaseInstance(context)
                dataBase.registryDao().addWeightRegistry(regitry)
            }
        }

        fun getAll(context: Context): List<Registry>? {
            val dataBase = initializeDatabaseInstance(context)
            CoroutineScope(Dispatchers.IO).launch {
                lista = dataBase.registryDao().getAllRegistry()
            }
            return if (lista != null){
                lista
            } else {
                null
            }
        }
    }
}