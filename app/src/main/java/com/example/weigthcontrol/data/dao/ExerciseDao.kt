package com.example.weigthcontrol.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weigthcontrol.data.model.Exercise

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise)

    @Query("SELECT * FROM exercise")
    suspend fun getAllExercises(): List<Exercise>

    @Query("SELECT * FROM exercise WHERE exercId = 0")
    suspend fun getFirstExercise(): Exercise

}