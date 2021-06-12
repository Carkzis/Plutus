package com.example.android.plutus

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.plutus.pcls.PclsCalcViewModel
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import java.lang.NullPointerException

@Suppress("DEPRECATION")
class PclsCalcViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Subject to be tested
    private lateinit var pclsCalcViewModel: PclsCalcViewModel

    @Before
    fun setupPclsViewModel() {
        pclsCalcViewModel = PclsCalcViewModel()
    }

    @Test(expected = NullPointerException::class)
    fun testLiveData_pclsCalculationNoDcFund_postResultsToLiveData() {
        // Set values
        pclsCalcViewModel.fullPension.value = "5,000.00"
        pclsCalcViewModel.commutationFactor.value = "20"
        pclsCalcViewModel.dcFund.value = ""

        // Call function
        pclsCalcViewModel.validateBeforeCalculation()

        // Test output values
        assertThat(pclsCalcViewModel.dbBenOutput.getOrAwaitValue().pcls, `is`("£25,000.00"))
        assertThat(pclsCalcViewModel.dbBenOutput.getOrAwaitValue().residualPension, `is`("£3,750.00"))
        assertThat(pclsCalcViewModel.dbBenOutput.getOrAwaitValue().dcFund, `is`("£0.00"))
        assertThat(pclsCalcViewModel.dbBenOutput.getOrAwaitValue().lta, `is`("9.30%"))

        // Test full pension benefits without commutation
        assertThat(pclsCalcViewModel.noPclsBenOutput.getOrAwaitValue().pcls, `is`("£0.00"))
        assertThat(pclsCalcViewModel.noPclsBenOutput.getOrAwaitValue().residualPension, `is`("£5,000.00"))
        assertThat(pclsCalcViewModel.noPclsBenOutput.getOrAwaitValue().dcFund, `is`("£0.00"))
        assertThat(pclsCalcViewModel.noPclsBenOutput.getOrAwaitValue().lta, `is`("9.31%"))

        // We expect this to be null, but will test anyway
        assertThat(pclsCalcViewModel.cmbBenOutput.getOrAwaitValue(), `is`(null))
    }

    @Test
    fun testLiveData_pclsCalculationWithDcFund_postResultsToLiveData() {
        // Set values, use a variety of entry styles
        pclsCalcViewModel.fullPension.value = "500"
        pclsCalcViewModel.commutationFactor.value = "15.0"
        pclsCalcViewModel.dcFund.value = "2,000."

        // Call function
        pclsCalcViewModel.validateBeforeCalculation()

        // Test db output values
        assertThat(pclsCalcViewModel.dbBenOutput.getOrAwaitValue().pcls, `is`("£2,307.69"))
        assertThat(pclsCalcViewModel.dbBenOutput.getOrAwaitValue().residualPension, `is`("£346.15"))
        assertThat(pclsCalcViewModel.dbBenOutput.getOrAwaitValue().dcFund, `is`("£2,000.00"))
        assertThat(pclsCalcViewModel.dbBenOutput.getOrAwaitValue().lta, `is`("1.03%"))

        // Test combined db and dc values, which should no longer be null
        assertThat(pclsCalcViewModel.cmbBenOutput.getOrAwaitValue().pcls, `is`("£2,923.08"))
        assertThat(pclsCalcViewModel.cmbBenOutput.getOrAwaitValue().residualPension, `is`("£438.46"))
        assertThat(pclsCalcViewModel.cmbBenOutput.getOrAwaitValue().dcFund, `is`("£0.00"))
        assertThat(pclsCalcViewModel.cmbBenOutput.getOrAwaitValue().lta, `is`("1.08%"))

        // Test full pension benefits without commutation
        assertThat(pclsCalcViewModel.noPclsBenOutput.getOrAwaitValue().pcls, `is`("£0.00"))
        assertThat(pclsCalcViewModel.noPclsBenOutput.getOrAwaitValue().residualPension, `is`("£500.00"))
        assertThat(pclsCalcViewModel.noPclsBenOutput.getOrAwaitValue().dcFund, `is`("£2,000.00"))
        assertThat(pclsCalcViewModel.noPclsBenOutput.getOrAwaitValue().lta, `is`("1.11%"))
    }

    @Test
    fun testLiveData_noPensionInput_postResultsToToastLiveData() {
        // Do not set any values
        // Call function
        pclsCalcViewModel.validateBeforeCalculation()

        // This will throw a TimeoutError if Toast value is not added to the LiveData,
        // meaning that the validation was passed (and so fail this JUnit test!)
        assertThat(pclsCalcViewModel.toastText.getOrAwaitValue(), `is`(not("null")))
    }

    @Test
    fun testLiveData_noCommutationFactorInput_postResultsToToastLiveData() {
        // Just set full pension value, no commutation factor
        pclsCalcViewModel.fullPension.value = "500"
        // Call function
        pclsCalcViewModel.validateBeforeCalculation()

        // This will throw a TimeoutError if Toast value is not added to the LiveData,
        // meaning that the validation was pass (and so fail this JUnit test!)
        assertThat(pclsCalcViewModel.toastText.getOrAwaitValue(), `is`(not("null")))
    }

    @Test
    fun testLiveData_pensionAndCommutationFactorEmpty_postResultsToToastLiveData() {
        // Pension value and commutation factor is not null, but is empty
        // (so the user typed, but pressed back space)
        pclsCalcViewModel.fullPension.value = ""
        pclsCalcViewModel.commutationFactor.value = ""
        // Call function
        pclsCalcViewModel.validateBeforeCalculation()

        // This will throw a TimeoutError if Toast value is not added to the LiveData,
        // meaning that the validation was pass (and so fail this JUnit test!)
        assertThat(pclsCalcViewModel.toastText.getOrAwaitValue(), `is`(not("null")))
    }

    @Test
    fun testLiveData_pensionAndCommutationFactorZero_postResultsToToastLiveData() {
        // Pension value and commutation factor set to zero
        pclsCalcViewModel.fullPension.value = "0"
        pclsCalcViewModel.commutationFactor.value = "0"
        // Call function
        pclsCalcViewModel.validateBeforeCalculation()

        // This will throw a TimeoutError if Toast value is not added to the LiveData,
        // meaning that the validation was pass (and so fail this JUnit test!)
        assertThat(pclsCalcViewModel.toastText.getOrAwaitValue(), `is`(not("null")))
    }

    @Test
    fun testLiveData_pensionAndCommutationFactorFullStops_postResultsToToastLiveData() {
        // Pension value and commutation factor set to zero
        pclsCalcViewModel.fullPension.value = "."
        pclsCalcViewModel.commutationFactor.value = "."
        // Call function
        pclsCalcViewModel.validateBeforeCalculation()

        // This will throw a TimeoutError if Toast value is not added to the LiveData,
        // meaning that the validation was pass (and so fail this JUnit test!)
        assertThat(pclsCalcViewModel.toastText.getOrAwaitValue(), `is`(not("null")))
    }

}
