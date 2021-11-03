package com.example.weigthcontrol.view.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weigthcontrol.data.model.Exercise
import com.example.weigthcontrol.databinding.ItemExerciseBinding
import com.example.weigthcontrol.view.ItemDeletedInterface
import com.example.weigthcontrol.viewmodel.ExerciseViewModel

class ExerciseAdapter(private val context: Context, private val exerciseList: List<Exercise>, val viewModel: ExerciseViewModel): RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>(), ItemDeletedInterface {

    inner class ExerciseViewHolder(val itemExerciseBinding: ItemExerciseBinding): RecyclerView.ViewHolder(itemExerciseBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ItemExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        with(holder) {
            with(exerciseList[position]){
                itemExerciseBinding.exerciseName.text = this.name
                itemExerciseBinding.deleteItem.setOnClickListener {
                    viewModel.deleteExercise(context, exerciseList[position])
                    onItemDeleted()
                }
            }
        }

    }

    override fun getItemCount() = exerciseList.size

    override fun onItemDeleted() {
        viewModel.getAllExercises(context)
    }
}