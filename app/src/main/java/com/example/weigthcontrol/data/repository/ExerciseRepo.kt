package com.example.weigthcontrol.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.weigthcontrol.data.dao.ExerciseDao
import com.example.weigthcontrol.data.database.WeigthRegistrysDataBase
import com.example.weigthcontrol.data.model.Exercise
import kotlinx.coroutines.*

class ExerciseRepo() {
    companion object {

        private lateinit var INSTANCE: WeigthRegistrysDataBase

        private fun initializeDatabaseInstance(context: Context) {
            INSTANCE = WeigthRegistrysDataBase.getInstanceDatabase(context)
        }

        fun insertExercise(context: Context, exercise: Exercise) {
            initializeDatabaseInstance(context)
            CoroutineScope(Dispatchers.IO).launch {
                INSTANCE.exerciseDao().insertExercise(exercise)
            }
        }
        private lateinit var exercise: Exercise
        fun getFirstExercise(context: Context): Exercise {
            initializeDatabaseInstance(context)
            runBlocking {
                exercise = INSTANCE.exerciseDao().getFirstExercise()
            }
            return exercise
        }

        private var list: List<Exercise>? = null
        fun getAllExercises(context: Context): List<Exercise>? {
            initializeDatabaseInstance(context)
            runBlocking {
                list = INSTANCE.exerciseDao().getAllExercises()
            }
            return if (list != null){
                list
            } else {
                null
            }
        }
    }
}