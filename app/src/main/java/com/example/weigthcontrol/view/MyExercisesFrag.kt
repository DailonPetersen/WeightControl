package com.example.weigthcontrol.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weigthcontrol.MainActivity
import com.example.weigthcontrol.R
import com.example.weigthcontrol.data.database.WeigthRegistrysDataBase
import com.example.weigthcontrol.data.model.ExerciseWithRegistries
import com.example.weigthcontrol.data.model.Registry
import com.example.weigthcontrol.data.repository.ExerciseDataSource
import com.example.weigthcontrol.data.repository.RegistryDataSource
import com.example.weigthcontrol.databinding.ItemExerciseBinding
import com.example.weigthcontrol.databinding.ItemRegistryBinding
import com.example.weigthcontrol.databinding.MyExercisesBinding
import com.example.weigthcontrol.view.recyclerview.GenericAdapter
import com.example.weigthcontrol.view.recyclerview.RegistryAdapter
import com.example.weigthcontrol.viewmodel.ExerciseViewModel

class MyExercisesFrag : Fragment() {

    private var _binding: MyExercisesBinding? = null
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

    private var lastItemClicked = -1
    private var lastVisibility: Int = -3
    private val viewPool = RecyclerView.RecycledViewPool()
    private var adapter: GenericAdapter<ExerciseWithRegistries, ItemExerciseBinding>? = null
    private var regAdapter: RegistryAdapter? = null
    //private var regAdapter: GenericAdapter<Registry, ItemRegistryBinding>? = null

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
        binding.OLDrecyclerView.adapter = adapter

        viewModel.mutableLiveDataExerciseWithRegistries.observe(viewLifecycleOwner) {
            binding.OLDrecyclerView.layoutManager = LinearLayoutManager(contextFragment)

            if (!it.isNullOrEmpty()) {
                //binding.emptyListView.visibility = View.GONE
                adapter = object : GenericAdapter<ExerciseWithRegistries, ItemExerciseBinding>(
                    contextFragment,
                    it
                ) {
                    override fun getLayoutResId() = R.layout.item_exercise

                    override fun onItemClick(model: ExerciseWithRegistries, position: Int) {
                        lastItemClicked = position
                        notifyItemChanged(lastItemClicked)
                    }

                    override fun onBindData(
                        model: ExerciseWithRegistries,
                        position: Int,
                        dataBinding: ItemExerciseBinding
                    ) {
                        dataBinding.exerciseName.setOnClickListener {
                            lastItemClicked = dataBinding.positionExercise.text.toString().toInt()
                            notifyItemChanged(lastItemClicked)
                        }

                        val layoutManager =
                            LinearLayoutManager(
                                contextFragment,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                        if (!model.listRegistries.isNullOrEmpty()) {
                            layoutManager.initialPrefetchItemCount = model.listRegistries.size
                        } else {
                            layoutManager.initialPrefetchItemCount = 0
                        }

                        regAdapter = RegistryAdapter(contextFragment, model, viewModel)

//                        regAdapter = object : GenericAdapter<Registry, ItemRegistryBinding>(contextFragment, model.listRegistries) {
//                            override fun getLayoutResId() = R.layout.item_registry
//
//                            override fun onBindData(
//                                model: Registry,
//                                position: Int,
//                                dataBinding: ItemRegistryBinding
//                            ) {
//                                dataBinding.registryDate.text = model.timestamp.subSequence(0, 10)
//                                dataBinding.registryWeigth.text = "${model.weight.toString()}kg"
//                                dataBinding.deleteItemRegistry.setOnClickListener {
//                                    viewModel.deleteRegistry(model)
//                                }
//                            }
//
//                            override fun onItemClick(model: Registry, position: Int) {
//                                Toast.makeText(contextFragment, "Clicado", Toast.LENGTH_LONG)
//                            }
//                        }

                        dataBinding.recyclerViewRegistry.layoutManager = layoutManager
                        dataBinding.recyclerViewRegistry.adapter = regAdapter
                        dataBinding.recyclerViewRegistry.setRecycledViewPool(viewPool)

                        dataBinding.exerciseName.text = model.exercise.name
                        dataBinding.positionExercise.text = position.toString()

                        if (lastItemClicked == position) {
                            if (lastVisibility == View.VISIBLE) {
                                lastVisibility = View.GONE
                                dataBinding.listRegistrys.visibility = View.GONE
                            } else {
                                lastVisibility = View.VISIBLE
                                dataBinding.listRegistrys.visibility = View.VISIBLE
                            }

                        }
                    }
                }
                adapter!!.notifyItemChanged(lastItemClicked)
            } else {
                binding.OLDrecyclerViewCard.visibility = View.GONE
            }

            binding.OLDrecyclerView.adapter = adapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getModelsRegistriesByExercise()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface ItemDeletedInterface {
    fun onItemDeleted()
}