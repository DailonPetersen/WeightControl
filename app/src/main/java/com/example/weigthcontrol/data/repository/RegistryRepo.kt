package com.example.weigthcontrol.data.repository

import com.example.weigthcontrol.data.dao.RegistryDao
import com.example.weigthcontrol.data.model.Registry

interface RegistryRepo {

    suspend fun getRegistriesByExercise(exerciseId: Int): List<Registry>?
    suspend fun deleteRegistry(registry: Registry)
    suspend fun insertRegistry(registry: Registry)
    suspend fun getAll(): List<Registry>?
    suspend fun deleteAllRegistrys()
}

class RegistryDataSource(private val registryDao: RegistryDao): RegistryRepo {

    override suspend fun getRegistriesByExercise(exerciseId: Int): List<Registry>? {
        return registryDao.getRegistriesByExercise(exerciseId)
    }

    override suspend fun deleteRegistry(registry: Registry) {
        registryDao.deleteRegistry(registry)
    }

    override suspend fun insertRegistry(registry: Registry) {
        registryDao.addWeightRegistry(registry)
    }

    override suspend fun getAll(): List<Registry>? {
        return registryDao.getAllRegistry()
    }

    override suspend fun deleteAllRegistrys() {
        registryDao.deleteAllRegistrys()
    }
}