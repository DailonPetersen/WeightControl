package com.example.weigthcontrol.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weigthcontrol.MainActivity
import com.example.weigthcontrol.R
import com.example.weigthcontrol.data.model.ExerciseWithRegistries
import com.example.weigthcontrol.databinding.ItemDataBinding
import com.example.weigthcontrol.databinding.MyDataBinding
import com.example.weigthcontrol.view.recyclerview.ExerciseAdapter
import com.example.weigthcontrol.view.recyclerview.GenericAdapter
import com.example.weigthcontrol.viewmodel.ExerciseViewModel

class MyData: Fragment() {

    private var _binding: MyDataBinding? = null
    private val binding get() = _binding!!

    private var _bindingItem: ItemDataBinding? = null
    private val bindingItem get() = _bindingItem!!

    private lateinit var contextFragment: MainActivity
    private val viewModel: ExerciseViewModel by viewModels()
    private var adapter: GenericAdapter<ExerciseWithRegistries, ItemDataBinding>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contextFragment = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MyDataBinding.inflate(inflater, container, false)
        binding.recyclerView.adapter = adapter

        viewModel.mutableLiveDataListExercise.observe(viewLifecycleOwner, {
            binding.recyclerView.layoutManager = LinearLayoutManager(contextFragment)

            val listaModels = viewModel.getModelsRegistriesByExercise(contextFragment, it)
            //TO DO -> se listaModels estiver nula, vai quebrar

            if (!listaModels.isNullOrEmpty()) {
                adapter = object : GenericAdapter<ExerciseWithRegistries, ItemDataBinding>(contextFragment, listaModels) {
                    override fun getLayoutResId(): Int {
                        return R.layout.item_data
                    }

                    override fun onBindData(model: ExerciseWithRegistries, position: Int, dataBinding: ItemDataBinding) {
                        dataBinding.exerciseNameData.text = model.exercise!!.name
                    }

                    override fun onItemClick(model: ExerciseWithRegistries, position: Int) {

                    }

                }
            } else {
                adapter = object : GenericAdapter<ExerciseWithRegistries, ItemDataBinding>(contextFragment, listaModels) {
                    override fun getLayoutResId(): Int {
                        return R.layout.item_data
                    }

                    override fun onBindData(model: ExerciseWithRegistries, position: Int, dataBinding: ItemDataBinding) {
                        dataBinding.exerciseNameData.text = model.exercise!!.name
                    }

                    override fun onItemClick(model: ExerciseWithRegistries, position: Int) {

                    }
                }
            }

            adapter!!.notifyDataSetChanged()
            binding.recyclerView.adapter = adapter

        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _bindingItem = null
    }
}