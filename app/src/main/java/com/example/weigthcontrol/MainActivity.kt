package com.example.weigthcontrol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weigthcontrol.databinding.ActivityMainBinding
import com.example.weigthcontrol.view.InsertExerciseDialogFrag
import com.example.weigthcontrol.view.OptionsFrag
import com.example.weigthcontrol.viewmodel.ExerciseViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ExerciseViewModel
    private lateinit var context: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)

        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, OptionsFrag()).addToBackStack("options").commit()

        binding.floatingActionButton.setOnClickListener {
            val dialog = InsertExerciseDialogFrag()
            dialog.show(supportFragmentManager, dialog.tag)
        }

        setContentView(binding.root)
    }
}