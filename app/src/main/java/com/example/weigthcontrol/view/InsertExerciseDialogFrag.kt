package com.example.weigthcontrol.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.weigthcontrol.MainActivity
import com.example.weigthcontrol.data.model.Exercise
import com.example.weigthcontrol.databinding.InsertExerciseDialogBinding
import com.example.weigthcontrol.viewmodel.ExerciseViewModel

class InsertExerciseDialogFrag: DialogFragment() {

    private var _binding: InsertExerciseDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExerciseViewModel by viewModels()
    private lateinit var contextFragment: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contextFragment = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InsertExerciseDialogBinding.inflate(inflater, container, false)

        binding.addNewExercise.setOnClickListener {
            val exerciseSelected = binding.spinnerExercises.selectedItem
            val exercise = Exercise(exerciseSelected.toString())
            viewModel.insertExercise(contextFragment, exercise)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}