package com.example.weigthcontrol.view.recyclerview

import android.animation.LayoutTransition
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.example.weigthcontrol.data.model.ExerciseWithRegistries
import com.example.weigthcontrol.databinding.ItemExerciseBinding
import com.example.weigthcontrol.view.*
import com.example.weigthcontrol.view.OnButtonClicked
import com.example.weigthcontrol.viewmodel.ExerciseViewModel

class ExerciseAdapter(private val context: Activity, private val exerciseList: List<ExerciseWithRegistries>, val viewModel: ExerciseViewModel): RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>(), ItemDeletedInterface {

    var lastItemClicked = -1
    private var adapter: RegistryAdapter? = null
    private val viewPool = RecycledViewPool()

    inner class ExerciseViewHolder(val itemExerciseBinding: ItemExerciseBinding): RecyclerView.ViewHolder(itemExerciseBinding.root) {
        init {
            val layoutTransition: LayoutTransition = itemExerciseBinding.itemList.getLayoutTransition()
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            itemExerciseBinding.exerciseName.setOnClickListener {
                lastItemClicked = bindingAdapterPosition
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ItemExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExerciseViewHolder(binding)
    }

    val listener = object: OnButtonClicked {
        override fun clicked(dialog: DialogFragment) {
            dialog.dismiss()
        }
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exerciseItem = exerciseList[position]
        holder.itemExerciseBinding.exerciseName.text = exerciseItem.exercise!!.name

        val layoutManager = LinearLayoutManager(holder.itemExerciseBinding.recyclerViewRegistry.context, LinearLayoutManager.VERTICAL, false)

        if (!exerciseItem.listRegistries.isNullOrEmpty()) {
            layoutManager.initialPrefetchItemCount = exerciseItem.listRegistries.size
        } else {
            layoutManager.initialPrefetchItemCount = 0
        }

        adapter = RegistryAdapter(context, exerciseItem, viewModel)

        holder.itemExerciseBinding.recyclerViewRegistry.layoutManager = layoutManager
        holder.itemExerciseBinding.recyclerViewRegistry.adapter = adapter
        holder.itemExerciseBinding.recyclerViewRegistry.setRecycledViewPool(viewPool)

        val fragmentManager = (holder.itemView.context as FragmentActivity).supportFragmentManager

//        holder.itemExerciseBinding.addRegistryButton.setOnClickListener {
//            val dialog = InsertRegistryDialog(context, listener)
//            dialog.show(fragmentManager, dialog.tag)
//        }

        if(lastItemClicked == position) {
            if (holder.itemExerciseBinding.listRegistrys.visibility == View.VISIBLE) {
                holder.itemExerciseBinding.listRegistrys.visibility = View.GONE
            } else {
                holder.itemExerciseBinding.listRegistrys.visibility = View.VISIBLE
            }
        } else {
            holder.itemExerciseBinding.listRegistrys.visibility = View.GONE
        }
    }

    override fun getItemCount() = exerciseList.size

    override fun onItemDeleted() {
        viewModel.getAllRegistry()
    }
}