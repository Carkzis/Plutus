package com.example.android.plutus

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.hamcrest.CoreMatchers
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