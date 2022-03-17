package com.example.weigthcontrol.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weigthcontrol.data.model.Registry

@Dao
interface RegistryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWeightRegistry(registry: Registry)

//    @Query("SELECT * FROM registry WHERE regId = :id")
//    suspend fun getRegistryById(id: Int): Registry

    @Query("SELECT * FROM registry ORDER BY timestamp ASC")
    fun getAllRegistry(): List<Registry>

    @Delete
    fun deleteRegistry(registry: Registry)

    @Query("SELECT * FROM registry WHERE exerciseId = :exerciseId ORDER BY timestamp ASC")
    suspend fun getRegistriesByExercise(exerciseId: Int): List<Registry>

    @Query("DELETE FROM registry WHERE regId > 0")
    suspend fun deleteAllRegistrys()
}