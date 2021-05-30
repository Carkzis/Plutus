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
        setUpButton()
        addDefaultResults()

    }

    private fun setUpStartDateDialog() {
        viewDataBinding.startDateEdittext.setOnClickListener {
            val calendar = setUpCalendar()
            DatePickerDialog(requireContext(), {
                view, y, m, d ->
                    viewModel.setStartDate(y, m, d)
            }, calendar.year, calendar.month, calendar.day).show()
        }
    }

    private fun setUpEndDateDialog() {
        viewDataBinding.endDateEdittext.setOnClickListener {
            val calendar = setUpCalendar()
            DatePickerDialog(requireContext(), {
                    view, y, m, d ->
                viewModel.setEndDate(y, m, d)
            }, calendar.year, calendar.month, calendar.day).show()
        }
    }

    data class CalendarInfo(val year: Int, val month: Int, val day: Int)

    private fun setUpCalendar(): CalendarInfo {
        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        Timber.w("Month is $month")
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return CalendarInfo(year, month, day)
    }

    private fun setUpButton() {
        viewDataBinding.dateCalcButton.setOnClickListener {
            viewModel.calculateDateDifferences()
        }
    }

    private fun addDefaultResults() {
        defaultResults = DateCalcResults(
            getString(R.string.years_results),
            getString(R.string.months_results),
            getString(R.string.weeks_results),
            getString(R.string.days_results),
            getString(R.string.years_months_results),
            getString(R.string.years_days_results),
            getString(R.string.tax_years_results),
            getString(R.string.sixth_aprils_results))
        viewModel.addDefaultResultsVM(defaultResults)
    }

}