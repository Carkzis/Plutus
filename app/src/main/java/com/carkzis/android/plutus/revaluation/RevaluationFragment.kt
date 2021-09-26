package com.carkzis.android.plutus.revaluation

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.carkzis.android.plutus.CalendarInfo
import com.carkzis.android.plutus.databinding.FragmentRevaluationBinding
import com.carkzis.android.plutus.util.showToast
import dagger.hilt.android.AndroidEntryPoint
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

        /*
         Set up data binding between the fragment and the layout. The lifecycleOwner observes
         the changes in LiveData in this databinding.
         */
        viewDataBinding = FragmentRevaluationBinding.inflate(inflater, container, false).apply {
            revaluationViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return viewDataBinding.root
    }

    /**
     * Used here to set up various observers/listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpButton()
        setUpLiveDataObservers()
        setUpStartDateDialog()
        setUpEndDateDialog()
        setUpToast()

    }

    /**
     * This sets up opening the DatePickerDialog when the edittext view for the start date
     * is clicked on.  This restricts the user to only providing valid dates.
     */
    private fun setUpStartDateDialog() {
        viewDataBinding.revalStartEdittext.setOnClickListener {
            val calendar = setUpCalendar(viewModel.startDateInfo.value!!)
            DatePickerDialog(requireContext(), {
                    _, y, m, d ->
                /*
                  Need to add 1 to month as it is indexed at 0 for some bizarre reason
                  (would be fine but years and days are not?)
                  */
                viewModel.setStartDate(y, m + 1, d)
            }, calendar.year, calendar.month, calendar.day).show()
        }
    }

    /**
     * This sets up opening the DatePickerDialog when the edittext view for the end date
     * is clicked on.  This restricts the user to only providing valid dates.
     */
    private fun setUpEndDateDialog() {
        viewDataBinding.revalEndEdittext.setOnClickListener {
            // Set up the initial date shown in the dialog.
            val calendar = setUpCalendar(viewModel.endDateInfo.value!!)
            DatePickerDialog(requireContext(), {
                    _, y, m, d ->
                viewModel.setEndDate(y, m + 1, d)
            }, calendar.year, calendar.month, calendar.day).show()
        }
    }

    /**
     * This sets up the initial date shown when opening the calendar.  It will either be the
     * current date if no start or end date has been previously selected, or the previous start
     * or end date selected (for example, if the user opens the calendar again after having
     * previously selected a date).
     */
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

    /**
     * Sets a click listener to the calculation button.
     */
    private fun setUpButton() {
        viewDataBinding.calculateRevalButton.setOnClickListener {
            viewModel.validateBeforeCalculation()
        }
    }

    /**
     * Sets up the observers for the CPI and RPI percentages.
     */
    private fun setUpLiveDataObservers() {
        viewModel.cpiPercentages.observe(viewLifecycleOwner, {})
        viewModel.rpiPercentages.observe(viewLifecycleOwner, {})
    }

    /**
     * Sets up the ability to show a toast once by observing the LiveData in the ViewModel.
     */
    private fun setUpToast() {
        viewModel.toastText.observe(viewLifecycleOwner, {
            it.getContextIfNotHandled()?.let { message ->
                context?.showToast(requireContext().getString(message))
            }
        })
    }

}