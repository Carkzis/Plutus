package com.example.android.plutus.revaluation

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.android.plutus.CalendarInfo
import com.example.android.plutus.databinding.FragmentRevaluationBinding
import com.example.android.plutus.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class RevaluationFragment : Fragment() {

    private val viewModel by viewModels<RevaluationViewModel>()

    private lateinit var viewDataBinding: FragmentRevaluationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewDataBinding = FragmentRevaluationBinding.inflate(inflater, container, false).apply {
            revaluationViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpButton()
        setUpLiveDataObservers()
        setUpStartDateDialog()
        setUpEndDateDialog()
        setUpToast()

    }

    private fun setUpStartDateDialog() {
        viewDataBinding.revalStartEdittext.setOnClickListener {
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
        viewDataBinding.revalEndEdittext.setOnClickListener {
            val calendar = setUpCalendar(viewModel.endDateInfo.value!!)
            DatePickerDialog(requireContext(), {
                    _, y, m, d ->
                viewModel.setEndDate(y, m + 1, d)
            }, calendar.year, calendar.month, calendar.day).show()
        }
    }

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
        viewDataBinding.calculateRevalButton.setOnClickListener {
            viewModel.validateBeforeCalculation()
        }
    }

    private fun setUpLiveDataObservers() {
        viewModel.cpiPercentages.observe(viewLifecycleOwner, {})
        viewModel.rpiPercentages.observe(viewLifecycleOwner, {})
    }

    private fun setUpToast() {
        viewModel.toastText.observe(viewLifecycleOwner, {
            it.getContextIfNotHandled()?.let { message ->
                context?.showToast(requireContext().getString(message))
            }
        })
    }

}