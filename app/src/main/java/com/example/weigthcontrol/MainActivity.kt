package com.example.weigthcontrol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.weigthcontrol.databinding.ActivityMainBinding
import com.example.weigthcontrol.view.InsertExerciseDialogFrag
import com.example.weigthcontrol.view.MyExercisesFrag

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var context: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)

        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, MyExercisesFrag())
            .addToBackStack("options").commit()

        val listener = object: OnButtonClicked {
            override fun clicked(dialog: DialogFragment) {
                dialog.dismiss()
            }

        }

        binding.floatingActionButton.setOnClickListener {
            val dialog = InsertExerciseDialogFrag(listener)
            dialog.show(supportFragmentManager, dialog.tag)
        }

        setContentView(binding.root)
    }
}

interface OnButtonClicked {
    fun clicked(dialog: DialogFragment)
}