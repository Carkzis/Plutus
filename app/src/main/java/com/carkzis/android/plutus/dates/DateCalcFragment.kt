package com.carkzis.android.plutus.dates

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.carkzis.android.plutus.CalendarInfo
import com.carkzis.android.plutus.DateCalcResults
import com.carkzis.android.plutus.databinding.FragmentDateCalcBinding
import com.carkzis.android.plutus.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Fragment for allowing the user to calculate the duration between two dates,
 * which will then show on the UI the duration in different units of time e.g. years, months.
 */
@AndroidEntryPoint
class DateCalcFragment : Fragment() {

    private val viewModel by viewModels<DateCalcViewModel>()

    private lateinit var viewDataBinding: FragmentDateCalcBinding
    private lateinit var defaultResults: DateCalcResults

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        /*
         Set up data binding between the fragment and the layout. The lifecycleOwner observes
         the changes in LiveData in this databinding.
         */
        viewDataBinding = FragmentDateCalcBinding.inflate(inflater, container, false).apply {
            dateCalcViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return viewDataBinding.root

    }

    /**
     * Used here to set up various observers/listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpStartDateDialog()
        setUpEndDateDialog()
        setUpButton()
        setUpToast()
        addDefaultResults()

    }

    /**
     * This sets up opening the DatePickerDialog when the edittext view for the start date
     * is clicked on.  This restricts the user to only providing valid dates.
     */
    private fun setUpStartDateDialog() {
        viewDataBinding.startDateEdittext.setOnClickListener {
            // Set up the initial date shown in the dialog.
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
        viewDataBinding.endDateEdittext.setOnClickListener {
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
        viewDataBinding.dateCalcButton.setOnClickListener {
            viewModel.validateBeforeCalculation()
        }
    }

    /**
     * Sets up the default date calculation results to 0 across the board.
     */
    private fun addDefaultResults() {
        defaultResults = DateCalcResults(
            0, 0, 0, 0, Pair(0, 0), Pair(0, 0), 0, 0)
        viewModel.addDefaultResultsVM(defaultResults)
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