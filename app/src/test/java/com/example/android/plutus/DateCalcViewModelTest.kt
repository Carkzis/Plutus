package com.example.android.plutus

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.plutus.dates.DateCalcViewModel
import com.example.android.plutus.util.DateCalcResults
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Suppress("DEPRECATION")
class DateCalcViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Subject to be tested
    private lateinit var dateCalcViewModel: DateCalcViewModel

    @Before
    fun setupPclsViewModel() {
        dateCalcViewModel = DateCalcViewModel()
    }

    @Test
    fun testLiveData_defaultValuesSetIfNoValues_postResultsToLiveData() {
        // Set values
        val defaultResults = DateCalcResults(10, 10,
            10, 10, Pair(10, 10),
            Pair(10, 10), 10, 10)

        // Call function
        dateCalcViewModel.addDefaultResultsVM(defaultResults)

        // This will throw a TimeoutError if Toast value is not added to the LiveData,
        // meaning that the validation was passed (and so fail this JUnit test!)
        assertThat(dateCalcViewModel.dateCalcResults.getOrAwaitValue().days,
            `is`(10)
        )
    }

    @Test
    fun testLiveData_defaultValuesNotSetIfNoValues_postResultsToLiveData() {
        // Set values
        val defaultResults = DateCalcResults(10, 10,
            10, 10, Pair(10, 10),
            Pair(10, 10), 10, 10)
        val calculatedResults = DateCalcResults(7, 7, 7, 7, Pair(7, 7),
            Pair(7, 7), 7, 7)

        // Call function
        // Hijacking the "addDefaultResultsVM" as it can set values to the results LiveData
        dateCalcViewModel.addDefaultResultsVM(calculatedResults)
        dateCalcViewModel.addDefaultResultsVM(defaultResults) // This should have no effect

        // This will throw a TimeoutError if Toast value is not added to the LiveData,
        // meaning that the validation was passed (and so fail this JUnit test!)
        assertThat(dateCalcViewModel.dateCalcResults.getOrAwaitValue().days,
            `is`(7)
        )
    }

    @Test
    fun testLiveData_noValuesSet_postResultsToToastLiveData() {
        // Do not set any values
        // Call function
        dateCalcViewModel.validateBeforeCalculation()

        // This will throw a TimeoutError if Toast value is not added to the LiveData,
        // meaning that the validation was passed (and so fail this JUnit test!)
        assertThat(dateCalcViewModel.toastText.getOrAwaitValue(),
            `is`(not("null"))
        )
    }

    @Test
    fun testLiveData_noStartDate_postResultsToToastLiveData() {
        // Set values
        dateCalcViewModel.endDateInfo.value = "31/05/2021"

        // Call function
        dateCalcViewModel.validateBeforeCalculation()

        // This will throw a TimeoutError if Toast value is added to the LiveData,
        // meaning that the validation was passed (and so fail this JUnit test!)
        assertThat(dateCalcViewModel.toastText.getOrAwaitValue(),
            `is`(not("null"))
        )
    }

    @Test
    fun testLiveData_noEndDate_postResultsToToastLiveData() {
        // Set values
        dateCalcViewModel.startDateInfo.value = "31/05/2021"

        // Call function
        dateCalcViewModel.validateBeforeCalculation()

        // This will throw a TimeoutError if Toast value is added to the LiveData,
        // meaning that the validation was passed (and so fail this JUnit test!)
        assertThat(dateCalcViewModel.toastText.getOrAwaitValue(),
            `is`(not("null"))
        )
    }

    @Test
    fun testLiveData_endDateBeforeStartDate_postResultsToToastLiveData() {
        // Set values
        dateCalcViewModel.startDateInfo.value = "31/05/2021"
        dateCalcViewModel.endDateInfo.value = "31/04/2021"

        // Call function
        dateCalcViewModel.validateBeforeCalculation()

        // This will throw a TimeoutError if Toast value is added to the LiveData,
        // meaning that the validation was passed (and so fail this JUnit test!)
        assertThat(dateCalcViewModel.toastText.getOrAwaitValue(),
            `is`(not("null"))
        )
    }

}