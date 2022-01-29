package com.example.weigthcontrol.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weigthcontrol.data.model.Exercise
import com.example.weigthcontrol.data.model.ExerciseWithRegistries
import com.example.weigthcontrol.data.model.Registry
import com.example.weigthcontrol.data.repository.ExerciseRepo
import com.example.weigthcontrol.data.repository.RegistryRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class ExerciseViewModel: ViewModel() {

    var listExercise: List<Exercise>? = null
    var listRegistry: List<Registry>? = null
    var listRegistriesByExercise: List<Registry>? = null
    var exercise: Exercise? = null

    val mutableLiveDataExercise = MutableLiveData<Exercise>()
    val mutableLiveDataListExercise = MutableLiveData<List<Exercise>>()
    val mutableLiveDataRegistry = MutableLiveData<List<Registry>>()
    val mutableLiveDataExerciseWithRegistries = MutableLiveData<List<ExerciseWithRegistries>>()

    fun getModelsRegistriesByExercise(context: Context, listExercise: List<Exercise>): List<ExerciseWithRegistries> {

        val listModels = mutableListOf<ExerciseWithRegistries>()

        for (exercise in listExercise) {
            val list = getRegistriesByExercise(context, exercise.exercId)
            lateinit var model: ExerciseWithRegistries

            if(!list.isNullOrEmpty()){
                model = ExerciseWithRegistries(exercise, list)
                listModels.add(model)
            }
            mutableLiveDataExerciseWithRegistries.value = listModels
        }
        return mutableLiveDataExerciseWithRegistries.value!!
    }

    fun getExeciseById(context: Context, exerciseId: Int): Exercise {
        val exercise = ExerciseRepo.getExerciseById(context, exerciseId)
        return exercise
    }

    fun getRegistriesByExercise(context: Context, exerciseId: Int): List<Registry>? {
        listRegistriesByExercise = RegistryRepo.getRegistriesByExercise(context, exerciseId)
        listRegistriesByExercise?.let {
            return listRegistriesByExercise
        }
        return null
    }

    fun insertExercise(context: Context, exercise: Exercise) {
        ExerciseRepo.insertExercise(context, exercise)
    }

    fun deleteExercise(context: Context, exercise: Exercise) {
        ExerciseRepo.deleteExercise(context, exercise)
    }

    fun deleteRegistry(context: Context, registry: Registry) {
        RegistryRepo.deleteRegistry(context, registry)
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
        getAllExercises(context)
    }

    fun getAllRegistry(context: Context): MutableLiveData<List<Registry>> {
        listRegistry = RegistryRepo.getAll(context)
        listRegistry?.let {
            mutableLiveDataRegistry.value = it
        }
        return mutableLiveDataRegistry
    }
}