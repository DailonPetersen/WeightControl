package com.example.weigthcontrol

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.math.MathUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weigthcontrol.data.model.Exercise
import com.example.weigthcontrol.data.repository.ExerciseRepo
import com.example.weigthcontrol.databinding.ActivityMainBinding
import com.example.weigthcontrol.view.recyclerview.ExerciseAdapter
import com.example.weigthcontrol.viewmodel.ExerciseViewModel
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ExerciseViewModel
    private lateinit var context: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)
        context = this
        viewModel.getAllExercises(context).observe(context, userListUpdateObserver)

        binding.addNewExercise.setOnClickListener {
            val exercise = Exercise(Random(2).nextInt(), binding.newExerciseInput.text.toString())
            viewModel.insertExercise(context, exercise)
        }

        setContentView(binding.root)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }

    private var userListUpdateObserver: Observer<List<Exercise>> =
        Observer<List<Exercise>> { it ->
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            binding.recyclerView.adapter = ExerciseAdapter(it)
        }
}