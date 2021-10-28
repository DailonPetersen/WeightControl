package com.example.weigthcontrol.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.weigthcontrol.data.model.Registry

@Dao
interface RegExerDao {

    @Query("SELECT * FROM registry")
    suspend fun getAllRegExer(): List<Registry>

}