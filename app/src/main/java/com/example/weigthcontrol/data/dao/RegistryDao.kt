package com.example.weigthcontrol.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weigthcontrol.data.model.Registry

@Dao
interface RegistryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWeightRegistry(registry: Registry)

//    @Query("SELECT * FROM registry WHERE regId = :id")
//    suspend fun getRegistryById(id: Int): Registry

    @Query("SELECT * FROM registry")
    suspend fun getAllRegistry(): List<Registry>
}