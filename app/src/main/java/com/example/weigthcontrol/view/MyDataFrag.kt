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
import com.example.weigthcontrol.data.database.WeigthRegistrysDataBase
import com.example.weigthcontrol.data.model.ExerciseWithRegistries
import com.example.weigthcontrol.data.repository.ExerciseDataSource
import com.example.weigthcontrol.data.repository.RegistryDataSource
import com.example.weigthcontrol.databinding.ItemDataBinding
import com.example.weigthcontrol.databinding.MyDataBinding
import com.example.weigthcontrol.view.recyclerview.GenericAdapter
import com.example.weigthcontrol.viewmodel.ExerciseViewModel
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MyDataFrag : Fragment() {

    private var _binding: MyDataBinding? = null
    private val binding get() = _binding!!

    private var _bindingItem: ItemDataBinding? = null

    private var lastItemClicked = -1
    private var lastVisibility: Int = -3
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

            if (!it.isNullOrEmpty()) {
                //binding.emptyListView.visibility = View.GONE
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
                                SimpleDateFormat("dd/MM/yyyy", Locale.ROOT).parse(reg.timestamp)
                            registries.add(DataPoint(date, reg.weight.toDouble()))
                        }

                        val lineGraph = LineGraphSeries(registries.toTypedArray())

                        dataBinding.exerciseNameData.text = model.exercise.name
                        dataBinding.positionExercise.text = position.toString()

                        if (lastItemClicked == position) {
                            if (lastVisibility == View.VISIBLE) {
                                lastVisibility = View.GONE
                                dataBinding.showGraph.visibility = View.GONE
                            } else {
                                lastVisibility = View.VISIBLE
                                dataBinding.graphData.addSeries(lineGraph)
                                dataBinding.graphData.gridLabelRenderer.labelFormatter =
                                    DateAsXAxisLabelFormatter(
                                        contextFragment,
                                        SimpleDateFormat("dd-MM", Locale.ROOT)
                                    )
                                dataBinding.graphData.viewport.isXAxisBoundsManual = true
                                dataBinding.graphData.gridLabelRenderer.setHumanRounding(false)
                                dataBinding.showGraph.visibility = View.VISIBLE
                            }
                        }
                    }

                    override fun onItemClick(model: ExerciseWithRegistries, position: Int) {
                        lastItemClicked = position
                        notifyItemChanged(lastItemClicked)
                    }
                }
                adapter!!.notifyItemChanged(lastItemClicked)
            } else {
                binding.recyclerViewCard.visibility = View.GONE
            }

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