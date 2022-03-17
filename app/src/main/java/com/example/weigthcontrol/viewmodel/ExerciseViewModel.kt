package com.example.weigthcontrol.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.weigthcontrol.data.database.WeigthRegistrysDataBase
import com.example.weigthcontrol.data.model.Exercise
import com.example.weigthcontrol.data.model.ExerciseWithRegistries
import com.example.weigthcontrol.data.model.Registry
import com.example.weigthcontrol.data.repository.ExerciseDataSource
import com.example.weigthcontrol.data.repository.ExerciseRepo
import com.example.weigthcontrol.data.repository.RegistryDataSource
import com.example.weigthcontrol.data.repository.RegistryRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ExerciseViewModel(
    val registryRepo: RegistryRepo,
    val exerciseRepo: ExerciseRepo
): ViewModel() {

    class ViewModelFactory(private val registryRepo: RegistryRepo, private val exerciseRepo: ExerciseRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExerciseViewModel::class.java)) {
                return ExerciseViewModel(registryRepo, exerciseRepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    var listExercise: List<Exercise>? = null
    var listRegistry: List<Registry>? = null
    var listRegistriesByExercise: List<Registry>? = null

    private val _mutableLiveDataExercise = MutableLiveData<List<Exercise>>()
    val mutableLiveDataExercise: LiveData<List<Exercise>>
        get() = _mutableLiveDataExercise

    private val _mutableLiveDataRegistry = MutableLiveData<List<Registry>>()
    val mutableLiveDataRegistry: LiveData<List<Registry>>
        get() = _mutableLiveDataRegistry

    private val _mutableLiveDataExerciseWithRegistries = MutableLiveData<List<ExerciseWithRegistries>>()
    val mutableLiveDataExerciseWithRegistries: LiveData<List<ExerciseWithRegistries>>
        get() = _mutableLiveDataExerciseWithRegistries

    fun getModelsRegistriesByExercise() {

        val listModels = mutableListOf<ExerciseWithRegistries>()

        val listExercise = getAllExercises()
        listExercise?.let {
            for (exercise in it) {
                val registriesByExercise = getRegistriesByExercise(exercise.exercId)
                if (!registriesByExercise.isNullOrEmpty()) {
                    listModels.add(ExerciseWithRegistries(exercise, registriesByExercise))
                }
            }
        }
        _mutableLiveDataExerciseWithRegistries.value = listModels

    }

//    fun getExeciseById(context: Context, exerciseId: Int): Exercise {
//        viewModelScope.launch(Dispatchers.IO {
//            val exercise = ExerciseRepo.getE                   xerciseById(context, exerciseId)
//        }
//    }

    fun getRegistriesByExercise(exerciseId: Int): List<Registry>? {
        runBlocking {
            listRegistriesByExercise = registryRepo.getRegistriesByExercise(exerciseId)
        }
        return if (!listRegistriesByExercise.isNullOrEmpty()) {
            listRegistriesByExercise
        } else null
    }

    fun insertExercise(exercise: Exercise) = viewModelScope.launch(Dispatchers.IO) {
        exerciseRepo.insertExercise(exercise)
    }

    fun deleteExercise( exercise: Exercise) = viewModelScope.launch(Dispatchers.IO) {
        exerciseRepo.deleteExercise(exercise)
    }

    fun deleteAllExercise() = viewModelScope.launch(Dispatchers.IO) {
        exerciseRepo.deleteAllExercise()
    }

    fun deleteAllRegistrys() = viewModelScope.launch(Dispatchers.IO) {
        registryRepo.deleteAllRegistrys()
    }

    fun deleteRegistry(registry: Registry) {
        viewModelScope.launch(Dispatchers.IO) {
            registryRepo.deleteRegistry(registry)
        }
    }

    fun getAllExercises(): List<Exercise>? {
        runBlocking {
            listExercise = exerciseRepo.getAllExercises()
        }
        return if (!listExercise.isNullOrEmpty()) {
            _mutableLiveDataExercise.value = listExercise
            listExercise
        } else null
    }

    fun insertRegistry(registry: Registry) = viewModelScope.launch(Dispatchers.IO) {
        registryRepo.insertRegistry(registry)
    }

    fun getAllRegistry(): List<Registry>? {
        viewModelScope.launch(Dispatchers.IO) {
            listRegistry = registryRepo.getAll()
        }
        return if (!listRegistry.isNullOrEmpty()) {
            _mutableLiveDataRegistry.value = listRegistry
            listRegistry
        } else null
    }
}