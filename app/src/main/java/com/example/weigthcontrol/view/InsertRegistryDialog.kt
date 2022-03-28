package com.example.weigthcontrol.view

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.weigthcontrol.MainActivity
import com.example.weigthcontrol.R
import com.example.weigthcontrol.data.database.WeigthRegistrysDataBase
import com.example.weigthcontrol.data.model.Exercise
import com.example.weigthcontrol.data.model.Registry
import com.example.weigthcontrol.data.repository.ExerciseDataSource
import com.example.weigthcontrol.data.repository.RegistryDataSource
import com.example.weigthcontrol.databinding.InsertRegistryDialogBinding
import com.example.weigthcontrol.viewmodel.ExerciseViewModel
import java.util.*

class InsertRegistryDialog(private val onButtonClickedReceipt: OnButtonClicked, private val exerciseId : Int?): DialogFragment() {

    private var _binding: InsertRegistryDialogBinding? = null
    private val binding get() = _binding!!
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

    private var listExercise: List<Exercise>? = null
    private lateinit var datePickerDialog: DatePickerDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contextFragment = context as MainActivity
        viewModel.getAllExercises()
    }

    private fun appendAtArray(array: Array<String>, element: String): Array<String> {
        val arrayFinal = arrayOfNulls<String>(array.size + 1)
        System.arraycopy(array, 0, arrayFinal, 0, array.size)
        arrayFinal[array.size] = element
        return if(!arrayFinal.contains(null)){
            arrayFinal as Array<String>
        } else {
            array
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InsertRegistryDialogBinding.inflate(inflater, container, false)
        listExercise = viewModel.mutableLiveDataExercise.value

        var listNames = arrayOf<String>()

        listExercise?.let {
            for (item in it) {
                listNames = appendAtArray(listNames, item.name)
            }
            val spinnerAdapter = ArrayAdapter(contextFragment, R.layout.support_simple_spinner_dropdown_item, listNames)
            binding.autoCompleteView.setAdapter(spinnerAdapter)

            if(exerciseId != null){
                binding.autoCompleteView.setSelection(exerciseId)
            }
        }

        return binding.root 
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendar = Calendar.getInstance()

        binding.pickDateButton.setOnClickListener {
            datePickerDialog.show()
        }

        binding.pickDateButton.setOnDateChangedListener { view, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
        }

        binding.insertRegistry.setOnClickListener {
            val exerciseName = binding.autoCompleteView.text.toString()
            var exerciseId: Int? = null
            listExercise?.let {
                for (item in it) {
                    if (item.name == exerciseName) {
                        exerciseId = item.exercId
                        break
                    }
                }
            }

            exerciseId?.let {
                val registry = Registry(DateFormat.format("dd/MM/yyyy", calendar).toString(), it, binding.inputWeigth.text.toString().toInt())
                viewModel.insertRegistry(registry)
            }

            onButtonClickedReceipt.clicked(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    fun initDatePicker() {
//        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
//
//            override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
//                val date = makeDateString(day, month, year)
//                binding.pickDateButton.text = date
//            }
//        }
//        val calendar = Calendar.getInstance()
//        val year = calendar.get(Calendar.YEAR)
//        val month = calendar.get(Calendar.MONTH)
//        val day = calendar.get(Calendar.DAY_OF_MONTH)
//        datePickerDialog = DatePickerDialog(context, R.style.MySpinnerDatePickerStyle, dateSetListener, year, month, day)
//    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return getMonthFormat(month).toString() + " " + day + " " + year
    }

    private fun getMonthFormat(month: Int): String {
        return when (month) {
            1 -> "JAN"
            2 -> "FEV"
            3 -> "MAR"
            4 -> "ABR"
            5 -> "MAI"
            6 -> "JUN"
            7 -> "JUL"
            8 -> "AGO"
            9 -> "SET"
            10 -> "OUT"
            11 -> "NOV"
            12 -> "DEZ"
            else -> "JAN"
        }
    }

    private fun getTodaysDate(): String {
        val cal = Calendar.getInstance()
        val year = cal[Calendar.YEAR]
        var month = cal[Calendar.MONTH]
        month += 1
        val day = cal[Calendar.DAY_OF_MONTH]
        return makeDateString(day, month, year)
    }
}