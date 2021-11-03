package com.example.weigthcontrol.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weigthcontrol.data.model.Exercise
import com.example.weigthcontrol.data.database.WeigthRegistrysDataBase
import com.example.weigthcontrol.data.model.Registry
import com.example.weigthcontrol.data.repository.ExerciseRepo
import com.example.weigthcontrol.data.repository.RegistryRepo

class ExerciseViewModel: ViewModel() {

    var listExercise: List<Exercise>? = null
    var listRegistry: List<Registry>? = null
    var exercise: Exercise? = null

    val mutableLiveDataExercise = MutableLiveData<Exercise>()
    val mutableLiveDataListExercise = MutableLiveData<List<Exercise>>()
    val mutableLiveDataRegistry = MutableLiveData<List<Registry>>()

    fun insertExercise(context: Context, exercise: Exercise) {
        ExerciseRepo.insertExercise(context, exercise)
        getAllExercises(context)
    }

    fun deleteExercise(context: Context, exercise: Exercise) {
        ExerciseRepo.deleteExercise(context, exercise)
    }

    fun getAllExercises(context: Context): MutableLiveData<List<Exercise>> {
        listExercise = ExerciseRepo.getAllExercises(context)
        listExercise?.let {
            mutableLiveDataListExercise.value = it
        }
        return mutableLiveDataListExercise
    }

    fun getFirstExercise(context: Context): MutableLiveData<Exercise> {
        exercise = ExerciseRepo.getFirstExercise(context)
        exercise?.let {
            mutableLiveDataExercise.value = it
        }
        return mutableLiveDataExercise
    }

    fun insertRegistry(context: Context, registry: Registry) {
        RegistryRepo.insertRegistry(context, registry)
    }

    fun getAllRegistry(context: Context): MutableLiveData<List<Registry>> {
        listRegistry = RegistryRepo.getAll(context)
        listRegistry?.let {
            mutableLiveDataRegistry.value = it
        }
        return mutableLiveDataRegistry
    }
}