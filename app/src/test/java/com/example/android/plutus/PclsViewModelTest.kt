package com.example.android.plutus

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import com.example.android.plutus.getOrAwaitValue
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import java.lang.NullPointerException
import java.util.concurrent.TimeoutException

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

    @Test(expected = NullPointerException::class)
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

        // We expect this to be null, but will test anyway
        assertThat(pclsViewModel.cmbBenOutput.getOrAwaitValue(), `is`(null))
    }

    @Test
    fun testLiveData_pclsCalculationWithDcFund_postResultsToLiveData() {
        // Set values, use a variety of entry styles
        pclsViewModel.fullPension.value = "500"
        pclsViewModel.commutationFactor.value = "15.0"
        pclsViewModel.dcFund.value = "2,000."

        // Call function
        pclsViewModel.validateBeforeCalculation()

        // Test db output values
        assertThat(pclsViewModel.dbBenOutput.getOrAwaitValue().pcls, `is`("£2,307.69"))
        assertThat(pclsViewModel.dbBenOutput.getOrAwaitValue().residualPension, `is`("£346.15"))
        assertThat(pclsViewModel.dbBenOutput.getOrAwaitValue().dcFund, `is`("£2,000.00"))
        assertThat(pclsViewModel.dbBenOutput.getOrAwaitValue().lta, `is`("1.03%"))

        // Test combined db and dc values, which should no longer be null
        assertThat(pclsViewModel.cmbBenOutput.getOrAwaitValue().pcls, `is`("£2,923.08"))
        assertThat(pclsViewModel.cmbBenOutput.getOrAwaitValue().residualPension, `is`("£438.46"))
        assertThat(pclsViewModel.cmbBenOutput.getOrAwaitValue().dcFund, `is`("£0.00"))
        assertThat(pclsViewModel.cmbBenOutput.getOrAwaitValue().lta, `is`("1.08%"))
    }

    @Test
    fun testLiveData_noPensionInput_postResultsToToastLiveData() {
        // Do not set any values
        // Call function
        pclsViewModel.validateBeforeCalculation()

        // This will throw a TimeoutError if Toast value is added to the LiveData,
        // meaning that the validation was pass (and so fail this JUnit test!)
        assertThat(pclsViewModel.toastText.getOrAwaitValue(), `is`(not("null")))
    }

    @Test
    fun testLiveData_noCommutationFactorInput_postResultsToToastLiveData() {
        // Just set full pension value, no commutation factor
        pclsViewModel.fullPension.value = "500"
        // Call function
        pclsViewModel.validateBeforeCalculation()

        // This will throw a TimeoutError if Toast value is added to the LiveData,
        // meaning that the validation was pass (and so fail this JUnit test!)
        assertThat(pclsViewModel.toastText.getOrAwaitValue(), `is`(not("null")))
    }

    @Test
    fun testLiveData_pensionAndCommutationFactorEmpty_postResultsToToastLiveData() {
        // Pension value and commutation factor is not null, but is empty
        // (so the user typed, but pressed back space)
        pclsViewModel.fullPension.value = ""
        pclsViewModel.commutationFactor.value = ""
        // Call function
        pclsViewModel.validateBeforeCalculation()

        // This will throw a TimeoutError if Toast value is added to the LiveData,
        // meaning that the validation was pass (and so fail this JUnit test!)
        assertThat(pclsViewModel.toastText.getOrAwaitValue(), `is`(not("null")))
    }

    @Test
    fun testLiveData_pensionAndCommutationFactorZero_postResultsToToastLiveData() {
        // Pension value and commutation factor set to zero
        pclsViewModel.fullPension.value = "0"
        pclsViewModel.commutationFactor.value = "0"
        // Call function
        pclsViewModel.validateBeforeCalculation()

        // This will throw a TimeoutError if Toast value is added to the LiveData,
        // meaning that the validation was pass (and so fail this JUnit test!)
        assertThat(pclsViewModel.toastText.getOrAwaitValue(), `is`(not("null")))
    }

    @Test
    fun testLiveData_pensionAndCommutationFactorFullStops_postResultsToToastLiveData() {
        // Pension value and commutation factor set to zero
        pclsViewModel.fullPension.value = "."
        pclsViewModel.commutationFactor.value = "."
        // Call function
        pclsViewModel.validateBeforeCalculation()

        // This will throw a TimeoutError if Toast value is added to the LiveData,
        // meaning that the validation was pass (and so fail this JUnit test!)
        assertThat(pclsViewModel.toastText.getOrAwaitValue(), `is`(not("null")))
    }

}
