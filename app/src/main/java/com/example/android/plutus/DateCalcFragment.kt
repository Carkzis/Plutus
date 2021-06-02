package com.example.android.plutus

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.android.plutus.databinding.FragmentDateCalcBinding
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class DateCalcFragment : Fragment() {

    private val viewModel by viewModels<DateCalcViewModel> {
        DateCalcViewModelFactory()
    }

    private lateinit var viewDataBinding: FragmentDateCalcBinding
    private lateinit var defaultResults: DateCalcResults

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        setUpButton()
        setUpToast()
        addDefaultResults()

    }

    private fun setUpStartDateDialog() {
        viewDataBinding.startDateEdittext.setOnClickListener {
            val calendar = setUpCalendar(viewModel.startDateInfo.value!!)
            DatePickerDialog(requireContext(), {
                    _, y, m, d ->
                // need to add 1 to month as it is indexed at 0 for some bizarre reason
                // (as years and days are not?)
                    viewModel.setStartDate(y, m + 1, d)
            }, calendar.year, calendar.month, calendar.day).show()
        }
    }

    private fun setUpEndDateDialog() {
        viewDataBinding.endDateEdittext.setOnClickListener {
            val calendar = setUpCalendar(viewModel.endDateInfo.value!!)
            DatePickerDialog(requireContext(), {
                    _, y, m, d ->
                viewModel.setEndDate(y, m + 1, d)
            }, calendar.year, calendar.month, calendar.day).show()
        }
    }

    data class CalendarInfo(val year: Int, val month: Int, val day: Int)

    private fun setUpCalendar(startOrEndDate: String): CalendarInfo {
        return if (startOrEndDate == "") {
            val calendar: Calendar = Calendar.getInstance()
            CalendarInfo(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        } else {
            val dateObj = LocalDate.parse(startOrEndDate,
                DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            CalendarInfo(
                dateObj.year,
                dateObj.monthValue - 1, // As Calendar month objects are indexed at 0
                dateObj.dayOfMonth)
        }
    }

    private fun setUpButton() {
        viewDataBinding.dateCalcButton.setOnClickListener {
            viewModel.validateBeforeCalculation()
        }
    }

    private fun addDefaultResults() {
        defaultResults = DateCalcResults(
            0, 0, 0, 0, Pair(0, 0), Pair(0, 0), 0, 0)
        viewModel.addDefaultResultsVM(defaultResults)
    }

    private fun setUpToast() {
        viewModel.toastText.observe(viewLifecycleOwner, {
            it.getContextIfNotHandled()?.let { message ->
                context?.showToast(requireContext().getString(message))
            }
        })
    }

}