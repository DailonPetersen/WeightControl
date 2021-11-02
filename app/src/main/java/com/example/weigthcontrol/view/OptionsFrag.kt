package com.example.weigthcontrol.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weigthcontrol.R
import com.example.weigthcontrol.databinding.MyExercisesBinding
import com.example.weigthcontrol.databinding.OptionsBinding

class OptionsFrag: Fragment() {

    private var _binding: OptionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OptionsBinding.inflate(inflater, container, false)

        binding.optionMyExercises.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainer, MyExercisesFrag()).commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}