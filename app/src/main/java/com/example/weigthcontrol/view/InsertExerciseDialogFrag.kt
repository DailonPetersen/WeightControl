package com.example.weigthcontrol.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.weigthcontrol.MainActivity
import com.example.weigthcontrol.data.model.Exercise
import com.example.weigthcontrol.databinding.InsertExerciseDialogBinding
import com.example.weigthcontrol.OnButtonClicked
import com.example.weigthcontrol.viewmodel.ExerciseViewModel

class InsertExerciseDialogFrag(val onButtonClickedReceipt: OnButtonClicked): DialogFragment() {

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addNewExercise.setOnClickListener {
            val exerciseSelected = binding.spinnerExercises.selectedItem
            val exercise = Exercise(exerciseSelected.toString())
            viewModel.insertExercise(contextFragment, exercise)
            onButtonClickedReceipt.clicked(this)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}