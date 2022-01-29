package com.example.weigthcontrol.view.recyclerview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.weigthcontrol.R
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
//                    viewModel.deleteExercise(context, exerciseList[position])
//                    onItemDeleted()

                    val layout = LinearLayout(context)
                    val editTextView = EditText(context)
                    val imageView = ImageView(context)
                    val datePickerView = DatePicker(context)

                    val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
                    layout.setBackgroundColor(Color.RED)
                    layout.layoutParams = layoutParams
                    layout.orientation = LinearLayout.HORIZONTAL

                    val editParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 7F)
                    editParams.setMargins(8,8,8,8)
                    editTextView.layoutParams = editParams

                    val imageViewParams = LinearLayout.LayoutParams(0, 80, 1F)
                    imageViewParams.gravity = (Gravity.CENTER_VERTICAL and Gravity.CENTER_HORIZONTAL)
                    imageView.setImageResource(R.drawable.ic_calendar)
                    imageView.layoutParams = imageViewParams

                    datePickerView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    datePickerView.visibility = View.GONE

                    layout.addView(editTextView)
                    layout.addView(imageView)
                    layout.addView(datePickerView)

                    itemExerciseBinding.itemList.findViewById<LinearLayout>(R.id.relativeLayout).addView(layout)
                }
            }
        }

    }

    override fun getItemCount() = exerciseList.size

    override fun onItemDeleted() {
        viewModel.getAllExercises(context)
    }
}