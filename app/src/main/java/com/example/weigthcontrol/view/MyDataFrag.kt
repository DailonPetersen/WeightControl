package com.example.weigthcontrol.view

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weigthcontrol.MainActivity
import com.example.weigthcontrol.R
import com.example.weigthcontrol.data.database.WeigthRegistrysDataBase
import com.example.weigthcontrol.data.model.ExerciseWithRegistries
import com.example.weigthcontrol.data.model.Registry
import com.example.weigthcontrol.data.repository.ExerciseDataSource
import com.example.weigthcontrol.data.repository.RegistryDataSource
import com.example.weigthcontrol.databinding.ItemDataBinding
import com.example.weigthcontrol.databinding.ItemExerciseBinding
import com.example.weigthcontrol.databinding.MyDataBinding
import com.example.weigthcontrol.view.recyclerview.GenericAdapter
import com.example.weigthcontrol.viewmodel.ExerciseViewModel
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.util.*


class MyDataFrag : Fragment() {

    private var _binding: MyDataBinding? = null
    private val binding get() = _binding!!

    private var _bindingItem: ItemDataBinding? = null

    private var lastItemClicked = -1

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

        viewModel.mutableLiveDataExerciseWithRegistries.observe(viewLifecycleOwner) {
            binding.recyclerView.layoutManager = LinearLayoutManager(contextFragment)

            adapter = object :
                GenericAdapter<ExerciseWithRegistries, ItemDataBinding>(contextFragment, it) {
                override fun getLayoutResId() = R.layout.item_data

                override fun onBindData(
                    model: ExerciseWithRegistries,
                    position: Int,
                    dataBinding: ItemDataBinding
                ) {
                    dataBinding.wrapperLayout.setOnClickListener {
                        lastItemClicked = dataBinding.positionExercise.text.toString().toInt()
                        notifyItemChanged(lastItemClicked)
                    }

                    val registries = mutableListOf<DataPoint>()

                    for (reg in model.listRegistries) {
                        val date =
                            SimpleDateFormat("dd/MM/yyyy-HH:mm", Locale.ROOT).parse(reg.timestamp)
                        registries.add(DataPoint(date, reg.weight.toDouble()))
                    }

                    val lineGraph = LineGraphSeries(registries.toTypedArray())

                    dataBinding.exerciseNameData.text = model.exercise.name
                    dataBinding.positionExercise.text = position.toString()

                    if (lastItemClicked == position) {
                        if (dataBinding.graphData.visibility == View.VISIBLE) {
                            dataBinding.graphData.visibility = View.GONE
                        } else {
                            dataBinding.graphData.addSeries(lineGraph)
                            dataBinding.graphData.visibility = View.VISIBLE
                        }
                    } else dataBinding.graphData.visibility = View.GONE
                }

                override fun onItemClick(model: ExerciseWithRegistries, position: Int) {
                    lastItemClicked = position
                    notifyItemChanged(lastItemClicked)
                }
            }

            adapter!!.notifyItemInserted(it.size)
            binding.recyclerView.adapter = adapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getModelsRegistriesByExercise()
    }

//    fun registriesToDataPoint(registries: ExerciseWithRegistries): Array<DataPoint> {
//        val list = arrayOf<DataPoint>()
//        for (item in registries.listRegistries!!) {
//            list[list.size] = DataPoint(item.timestamp as Double, item.weight as Double)
//        }
//        return list
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _bindingItem = null
    }
}