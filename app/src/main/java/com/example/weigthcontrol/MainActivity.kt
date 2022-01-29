package com.example.weigthcontrol

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weigthcontrol.databinding.ActivityMainBinding
import com.example.weigthcontrol.viewmodel.ExerciseViewModel
import androidx.viewpager2.widget.ViewPager2
import com.example.weigthcontrol.view.*
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ExerciseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setupViews()
//        val exer = listOf<Exercise>(
//            Exercise("um"),
//            Exercise("dois"),
//            Exercise("tres"),
//            Exercise("quatro")
//        )
//
//        val reg = listOf<Registry>(
//            Registry(DateFormat.format("dd-MM-yyyy", Date()).toString(), 1, 20),
//            Registry(DateFormat.format("dd-MM-yyyy", Date()).toString(), 3, 20),
//            Registry(DateFormat.format("dd-MM-yyyy", Date()).toString(), 3, 20),
//            Registry(DateFormat.format("dd-MM-yyyy", Date()).toString(), 4, 20),
//        )
//
//        for (item in exer) {
//            viewModel.insertExercise(this, item)
//        }
//
//        for (item in reg) {
//            viewModel.insertRegistry(this, item)
//        }
//
//        supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.out)
//            .add(R.id.fragmentContainer, OptionsFrag())
//            .addToBackStack("options").commit()


        val listener = object : OnButtonClicked {
            override fun clicked(dialog: DialogFragment) {
                dialog.dismiss()
            }
        }

        binding.floatingButtonNewRegistry.setOnClickListener {
            val dialog = InsertRegistryDialog(this, listener)
            dialog.show(supportFragmentManager, dialog.tag)
        }

        binding.floatingButtonNewExercise.setOnClickListener {
            val dialog = InsertExerciseDialogFrag(listener)
            dialog.show(supportFragmentManager, dialog.tag)
        }

        setContentView(binding.root)
    }

    private fun setupViews() {
        val viewPager = binding.viewPager
        val tabOptions = binding.tabOptions

        val adapterData = TabViewPagerAdapterData(this)
        binding.viewPager.adapter = adapterData

        TabLayoutMediator(tabOptions, viewPager) {tab, position ->
            tab.text = adapterData.tabs[position]
        }.attach()
    }

    class TabViewPagerAdapterData(fragment: FragmentActivity): FragmentStateAdapter(fragment) {
        val tabs = arrayOf("Meus dados", "Meus Exercicios")
        private val fragments = arrayOf(MyDataFrag(), MyExercisesFrag())

        override fun getItemCount() = fragments.size

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }

    class TabViewPagerAdapterAdd(fragment: FragmentActivity): FragmentStateAdapter(fragment) {
        val tabs = arrayOf("Novo Exercicio", "Novo Peso")

        val listener = object : OnButtonClicked {
            override fun clicked(dialog: DialogFragment) {
                dialog.dismiss()
            }
        }

        private val dialogFragment = arrayOf(InsertExerciseDialogFrag(listener), InsertRegistryDialog(fragment.applicationContext, listener))
        override fun getItemCount() = dialogFragment.size

        override fun createFragment(position: Int): Fragment {
            return dialogFragment[position]
        }

    }
}