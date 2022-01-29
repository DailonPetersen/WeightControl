package com.example.weigthcontrol.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weigthcontrol.MainActivity
import com.example.weigthcontrol.data.model.Exercise
import com.example.weigthcontrol.data.model.ExerciseWithRegistries
import com.example.weigthcontrol.data.model.Registry
import com.example.weigthcontrol.databinding.MyExercisesBinding
import com.example.weigthcontrol.view.recyclerview.ExerciseAdapter
import com.example.weigthcontrol.viewmodel.ExerciseViewModel
import java.util.*

class MyExercisesFrag: Fragment() {

    private var _binding: MyExercisesBinding? = null
    private val binding get() = _binding!!
    private lateinit var contextFragment: MainActivity
    private val viewModel: ExerciseViewModel by viewModels()
    private var adapter: ExerciseAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contextFragment = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MyExercisesBinding.inflate(inflater, container, false)
        binding.recyclerView.adapter = adapter

        viewModel.mutableLiveDataListExercise.observe(viewLifecycleOwner, {
            binding.recyclerView.layoutManager = LinearLayoutManager(contextFragment)
            val listaModels = viewModel.getModelsRegistriesByExercise(contextFragment, it)
            //TO DO -> se listaModels estiver nula, vai quebrar

            if (!listaModels.isNullOrEmpty()) {
                adapter = ExerciseAdapter(contextFragment, listaModels, viewModel)
            } else {
                adapter = ExerciseAdapter(contextFragment, listOf(), viewModel)
            }

            adapter!!.notifyDataSetChanged()
            binding.recyclerView.adapter = adapter
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllExercises(contextFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface ItemDeletedInterface {
    fun onItemDeleted()
}