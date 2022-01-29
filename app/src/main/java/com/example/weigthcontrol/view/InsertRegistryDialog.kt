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
import com.example.weigthcontrol.R
import com.example.weigthcontrol.data.model.Exercise
import com.example.weigthcontrol.data.model.Registry
import com.example.weigthcontrol.databinding.InsertRegistryDialogBinding
import com.example.weigthcontrol.viewmodel.ExerciseViewModel
import java.util.*

class InsertRegistryDialog(private val context: Context, private val onButtonClickedReceipt: OnButtonClicked): DialogFragment() {

    private var _binding: InsertRegistryDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExerciseViewModel by viewModels()
    private var listExercise = MutableLiveData<List<Exercise>>()
    private lateinit var datePickerDialog: DatePickerDialog

    private fun appendAtArray(array: Array<String>, element: String): Array<String> {
        val arrayFinal = arrayOfNulls<String>(array.size + 1)
        System.arraycopy(array, 0, arrayFinal, 0, array.size)
        arrayFinal[array.size] = element
        if(!arrayFinal.contains(null)){
            return arrayFinal as Array<String>
        } else {
            return array
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initDatePicker()
        _binding = InsertRegistryDialogBinding.inflate(inflater, container, false)
        listExercise = viewModel.getAllExercises(context)

        binding.pickDateButton.text = getTodaysDate()
        var listNames = arrayOf<String>()
        for (item in listExercise.value!!) {
            listNames = appendAtArray(listNames, item.name)
        }
        val spinnerAdapter = ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, listNames)
        binding.autoCompleteView.setAdapter(spinnerAdapter)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pickDateButton.setOnClickListener {
            datePickerDialog.show()
        }

        binding.insertRegistry.setOnClickListener {
            val exerciseName = binding.autoCompleteView.text.toString()
            var exerciseId: Int? = null
            for (item in listExercise.value!!) {
                if (item.name == exerciseName) {
                    exerciseId = item.exercId
                    break
                }
            }

            exerciseId?.let {
                val registry = Registry(DateFormat.format("dd/MM/yyyy", Date()).toString(), exerciseId, binding.inputWeigth.text.toString().toInt())
                viewModel.insertRegistry(context, registry)
            }

            onButtonClickedReceipt.clicked(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun initDatePicker() {
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {

            override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
                val date = makeDateString(day, month, year)
                binding.pickDateButton.text = date
            }
        }
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        datePickerDialog = DatePickerDialog(context, R.style.MySpinnerDatePickerStyle, dateSetListener, year, month, day)
    }

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