package com.example.android.plutus

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.android.plutus.databinding.FragmentDateCalcBinding
import java.util.*

class DateCalcFragment : Fragment() {

    private val viewModel by viewModels<DateCalcViewModel> {
        DateCalcViewModelFactory()
    }

    private lateinit var viewDataBinding: FragmentDateCalcBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentDateCalcBinding.inflate(inflater, container, false).apply {
            dateCalcViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return viewDataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpStartDateDialog()
        setUpEndDateDialog()

    }

    private fun setUpStartDateDialog() {
        viewDataBinding.startDateEdittext.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(requireContext(), {
                view, y, m, d ->
                    viewModel.setStartDate(y, m, d)
            }, year, month, day).show()
        }
    }

    private fun setUpEndDateDialog() {
        viewDataBinding.endDateEdittext.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(requireContext(), {
                    view, y, m, d ->
                viewModel.setEndDate(y, m, d)
            }, year, month, day).show()
        }
    }

}