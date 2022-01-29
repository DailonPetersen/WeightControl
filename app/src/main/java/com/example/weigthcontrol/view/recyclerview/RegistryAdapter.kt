package com.example.weigthcontrol.view.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.weigthcontrol.data.model.Exercise
import com.example.weigthcontrol.data.model.Registry
import com.example.weigthcontrol.databinding.ItemRegistryBinding
import com.example.weigthcontrol.viewmodel.ExerciseViewModel
import android.text.format.DateFormat
import com.example.weigthcontrol.data.model.ExerciseWithRegistries
import java.util.*

class RegistryAdapter(private val context: Context, private val item: ExerciseWithRegistries, val viewModel: ExerciseViewModel): RecyclerView.Adapter<RegistryAdapter.RegistryViewHolder>() {

    private val limit = 3

    inner class RegistryViewHolder(val itemRegistryBinding: ItemRegistryBinding): RecyclerView.ViewHolder(itemRegistryBinding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RegistryAdapter.RegistryViewHolder {
        val binding = ItemRegistryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RegistryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        if (item.listRegistries != null) {
            if(item.listRegistries.size > limit) {
                return limit
            } else
                return item.listRegistries.size
        } else {
            return -0
        }
    }

    override fun onBindViewHolder(holder: RegistryViewHolder, position: Int) {

        if (!item.listRegistries.isNullOrEmpty()) {
            val registryItem = item.listRegistries.get(position)

            with(holder) {
                itemRegistryBinding.registryDate.text = registryItem.timestamp
                itemRegistryBinding.registryWeigth.text = registryItem.weight.toString() + "kg"

                itemRegistryBinding.deleteItemRegistry.setOnClickListener {
                    viewModel.deleteRegistry(context, registryItem)
                    viewModel.getAllExercises(context)
                }
            }
        }

    }
}