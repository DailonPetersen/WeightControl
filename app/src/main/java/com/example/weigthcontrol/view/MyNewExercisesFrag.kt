package com.example.weigthcontrol.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weigthcontrol.MainActivity
import com.example.weigthcontrol.R
import com.example.weigthcontrol.data.database.WeigthRegistrysDataBase
import com.example.weigthcontrol.data.model.Exercise
import com.example.weigthcontrol.data.repository.ExerciseDataSource
import com.example.weigthcontrol.data.repository.RegistryDataSource
import com.example.weigthcontrol.databinding.*
import com.example.weigthcontrol.view.recyclerview.GenericAdapter
import com.example.weigthcontrol.viewmodel.ExerciseViewModel

class MyNewExercisesFrag : Fragment() {

    private var _binding: MyNewExerciseBinding? = null
    private val binding get() = _binding!!
    private lateinit var contextFragment: MainActivity

    private val viewModel: ExerciseViewModel by viewModels(
        factoryProducer = {
            val database = WeigthRegistrysDataBase.getInstanceDatabase(contextFragment)

            ExerciseViewModel.ViewModelFactory(
                registryRepo = RegistryDataSource(database.registryDao()),
                exerciseRepo = ExerciseDataSource(database.exerciseDao())
            )
        }
    )

    private var adapter: GenericAdapter<Exercise, NewItemExerciseBinding>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contextFragment = context as MainActivity
    }

    private val listener = object : OnButtonClicked {
        override fun clicked(dialog: DialogFragment) {
            dialog.dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MyNewExerciseBinding.inflate(inflater, container, false)
        binding.recyclerView.adapter = adapter

        viewModel.mutableLiveDataExercise.observe(viewLifecycleOwner) {
            binding.recyclerView.layoutManager = LinearLayoutManager(contextFragment)

            if (!it.isNullOrEmpty()) {
                adapter = object : GenericAdapter<Exercise, NewItemExerciseBinding>(
                    contextFragment,
                    it
                ) {
                    override fun getLayoutResId() = R.layout.new_item_exercise

                    override fun onBindData(
                        model: Exercise,
                        position: Int,
                        dataBinding: NewItemExerciseBinding
                    ) {
                        dataBinding.exerciseName.text = model.name
                        dataBinding.addWeigthButton.setOnClickListener {
                            val dialog = InsertRegistryDialog(listener, model.exercId)
                            dialog.show(contextFragment.supportFragmentManager, dialog.tag)
                        }
                    }

                    override fun onItemClick(model: Exercise, position: Int) {
                        Toast.makeText(contextFragment, model.name, Toast.LENGTH_LONG).show()
                    }
                }
            }
            binding.recyclerView.adapter = adapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllExercises()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
