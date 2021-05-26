package com.example.android.plutus

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import com.example.android.plutus.getOrAwaitValue
import org.junit.Rule

@Suppress("DEPRECATION")
class PclsViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Subject to be tested
    private lateinit var pclsViewModel: PclsViewModel

    @Before
    fun setupPclsViewModel() {
        pclsViewModel = PclsViewModel()
    }

    @Test
    fun testLiveData_pclsCalculationNoDcFund_postResultsToLiveData() {
        // Set values
        pclsViewModel.fullPension.value = "5,000.00"
        pclsViewModel.commutationFactor.value = "20"
        pclsViewModel.dcFund.value = ""

        // Call function
        pclsViewModel.validateBeforeCalculation()

        // Test output values
        assertThat(pclsViewModel.dbBenOutput.getOrAwaitValue().pcls, `is`("£25,000.00"))
        assertThat(pclsViewModel.dbBenOutput.getOrAwaitValue().residualPension, `is`("£3,750.00"))
        assertThat(pclsViewModel.dbBenOutput.getOrAwaitValue().dcFund, `is`("£0.00"))
        assertThat(pclsViewModel.dbBenOutput.getOrAwaitValue().lta, `is`("9.30%"))
    }

}
