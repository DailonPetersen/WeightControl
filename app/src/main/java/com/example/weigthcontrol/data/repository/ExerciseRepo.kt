package com.example.weigthcontrol.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weigthcontrol.data.dao.ExerciseDao
import com.example.weigthcontrol.data.database.WeigthRegistrysDataBase
import com.example.weigthcontrol.data.model.Exercise
import kotlinx.coroutines.*

interface ExerciseRepo {

    suspend fun getAllExercises(): List<Exercise>?
    suspend fun getExerciseById(exerciseId: Int): Exercise
    suspend fun insertExercise(exercise: Exercise)
    suspend fun deleteExercise(exercise: Exercise)
    suspend fun deleteAllExercise()
}

class ExerciseDataSource(private val exerciseDao: ExerciseDao): ExerciseRepo {

    override suspend fun getAllExercises(): List<Exercise>? {
        return exerciseDao.getAllExercises()
    }

    override suspend fun getExerciseById(exerciseId: Int): Exercise {
        return exerciseDao.getExerciseById(exerciseId)
    }

    override suspend fun insertExercise(exercise: Exercise) {
        exerciseDao.insertExercise(exercise)
    }

    override suspend fun deleteExercise(exercise: Exercise) {
        exerciseDao.deleteExercise(exercise)
    }

    override suspend fun deleteAllExercise() {
        exerciseDao.deleteAllExercise()
    }

}