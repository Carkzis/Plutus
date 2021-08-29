package com.carkzis.android.plutus.revaluation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.carkzis.android.plutus.data.DatabaseCpiPct
import com.carkzis.android.plutus.data.DatabaseRpiPct
import com.carkzis.android.plutus.data.FakeRepository
import com.carkzis.android.plutus.revaluation.RevaluationViewModel
import com.carkzis.android.plutus.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter

@ExperimentalCoroutinesApi
class RevaluationViewModelTest {

    private lateinit var inflationRepository: FakeRepository

    // This is from the kotlin docs, to allow access to Dispatcher.Main in testing.
    private val dispatcher = TestCoroutineDispatcher()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Subject to be tested
    private lateinit var revaluationViewModel: RevaluationViewModel

    private val endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    @Before
    fun setUpRevaluationViewModel() {
        Dispatchers.setMain(dispatcher)
        inflationRepository = FakeRepository()
        val oldCpiRates = inflationRepository.cpiDatabaseRates.value?.toMutableList()
        val oldRpiRates = inflationRepository.rpiDatabaseRates.value?.toMutableList()
        // I am making sure that the latest year added to the end of the rates.
        oldCpiRates?.add(
            DatabaseCpiPct("", "3.0", "",
            Year.now().toString(),"September", "", "", "", "")
        )
        oldRpiRates?.add(
            DatabaseRpiPct("", "3.0", "",
            Year.now().toString(),"September", "", "", "", "")
        )
        inflationRepository.cpiDatabaseRates.value = oldCpiRates
        inflationRepository.rpiDatabaseRates.value = oldRpiRates
        revaluationViewModel = RevaluationViewModel(inflationRepository)
        revaluationViewModel.cpiPercentages.getOrAwaitValue()
        revaluationViewModel.rpiPercentages.getOrAwaitValue()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLiveData_noValuesSet_postResultsToToastLiveData() {
        // Do not set any values
        // Call function
        revaluationViewModel.validateBeforeCalculation()

        // This checks that a Toast was displayed
        assertThat(revaluationViewModel.toastText.getOrAwaitValue(),
            `is`(not("null"))
        )
    }

    @Test
    fun testLiveData_noStartDate_postResultsToToastLiveData() {
        // Set values
        revaluationViewModel.endDateInfo.value = "31/05/2021"

        // Call function
        revaluationViewModel.validateBeforeCalculation()

        // This checks that a Toast was displayed
        assertThat(revaluationViewModel.toastText.getOrAwaitValue(),
            `is`(not("null"))
        )
    }

    @Test
    fun testLiveData_noEndDate_postResultsToToastLiveData() {
        // Set values
        revaluationViewModel.startDateInfo.value = "31/05/2021"

        // Call function
        revaluationViewModel.validateBeforeCalculation()

        // This checks that a Toast was displayed
        assertThat(revaluationViewModel.toastText.getOrAwaitValue(),
            `is`(not("null"))
        )
    }

    @Test
    fun testLiveData_endDateBeforeStartDate_postResultsToToastLiveData() {
        // Set values
        revaluationViewModel.startDateInfo.value = "31/05/2021"
        revaluationViewModel.endDateInfo.value = "31/04/2021"

        // Call function
        revaluationViewModel.validateBeforeCalculation()

        // This checks that a Toast was displayed
        assertThat(revaluationViewModel.toastText.getOrAwaitValue(),
            `is`(not("null"))
        )
    }

    @Test
    fun testLiveData_noDataAvailable_postResultsToToastLiveData() {
        // Set values
        revaluationViewModel.startDateInfo.value = "31/05/2017"
        revaluationViewModel.endDateInfo.value = endDate
        inflationRepository.setToEmpty()

        revaluationViewModel.cpiPercentages = inflationRepository.getSeptemberCpi()
        revaluationViewModel.rpiPercentages = inflationRepository.getSeptemberRpi()

        revaluationViewModel.cpiPercentages.getOrAwaitValue()
        revaluationViewModel.rpiPercentages.getOrAwaitValue()

        // Call function
        revaluationViewModel.validateBeforeCalculation()

        // This checks that a Toast was displayed
        assertThat(revaluationViewModel.toastText.getOrAwaitValue(),
            `is`(not("null"))
        )

        // Test the actual Toast value.
        assertThat(revaluationViewModel.toastTest.getOrAwaitValue(), `is`("Update Inflation First"))
    }

    @Test
    fun testLiveData_dataOutOfDate_postResultsToToastLiveData() {
        // Set values
        revaluationViewModel.startDateInfo.value = "31/05/2011"
        revaluationViewModel.endDateInfo.value = "31/05/2011"

        // We clear the data for the CPI and RPI rates, and then add out of date data.
        inflationRepository.setToEmpty()
        val oldCpiRates = inflationRepository.cpiDatabaseRates.value?.toMutableList()
        val oldRpiRates = inflationRepository.rpiDatabaseRates.value?.toMutableList()
        // I am making sure that the latest year added to the end of the rates.
        oldCpiRates?.add(DatabaseCpiPct("", "", "",
            "1900","September", "", "", "", ""))
        oldRpiRates?.add(
            DatabaseRpiPct("", "", "",
                "1900","September", "", "", "", "")
        )
        inflationRepository.cpiDatabaseRates.value = oldCpiRates
        inflationRepository.rpiDatabaseRates.value = oldRpiRates
        revaluationViewModel.cpiPercentages = inflationRepository.getSeptemberCpi()
        revaluationViewModel.rpiPercentages = inflationRepository.getSeptemberRpi()

        revaluationViewModel.cpiPercentages.getOrAwaitValue()
        revaluationViewModel.rpiPercentages.getOrAwaitValue()

        // Call function
        revaluationViewModel.validateBeforeCalculation()

        // This checks that a Toast was displayed
        assertThat(revaluationViewModel.toastText.getOrAwaitValue(),
            `is`(not("null"))
        )

        // The calculation will say an update is required based on today's date, but the calculation
        // will be attempted regardless, and a toast will inform the
        // user the request is too far ahead.
        assertThat(revaluationViewModel.toastTest.getOrAwaitValue(), `is`("Too Far Ahead"))

    }

    @Test
    fun testLiveData_resultsCalculated_resultsForCpiInflationAre1point0() {
        // Set values
        revaluationViewModel.startDateInfo.value = "31/05/2021"
        revaluationViewModel.endDateInfo.value = endDate

        // Call function
        revaluationViewModel.validateBeforeCalculation()

        // This should be 1.0
        assertThat(revaluationViewModel.revalCalcResults.getOrAwaitValue().cpiHigh, `is`(1.0))
    }

    @Test
    fun testLiveData_revaluationTooFarAhead_resultsForCpiInflationAreZero() {
        // Set values
        revaluationViewModel.startDateInfo.value = "31/05/2021"
        // Create a future date, that will always be in the future.
        val futureDate = LocalDate.ofYearDay(Year.now().toString().toInt() + 3, 1)
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        revaluationViewModel.endDateInfo.value = futureDate

        // Call function
        revaluationViewModel.validateBeforeCalculation()

        // This should be 1.0
        assertThat(revaluationViewModel.revalCalcResults.getOrAwaitValue().cpiHigh, `is`(0.0))

        // This checks that a Toast was displayed
        assertThat(revaluationViewModel.toastText.getOrAwaitValue(),
            `is`(not("null"))
        )

        // Test the actual Toast value.
        assertThat(revaluationViewModel.toastTest.getOrAwaitValue(), `is`("Too Far Ahead"))

        // This should be 1.0
        assertThat(revaluationViewModel.revalCalcResults.getOrAwaitValue().cpiHigh, `is`(0.0))
        assertThat(revaluationViewModel.revalCalcResults.getOrAwaitValue().rpiHigh, `is`(0.0))

    }

    @Test
    fun testLiveData_cpiAndRpiMisAligned_returnToastMessage() {
        // Set values
        revaluationViewModel.startDateInfo.value = endDate
        revaluationViewModel.endDateInfo.value = endDate

        // We clear the data for the CPI and RPI rates, and then add out of date data.
        inflationRepository.setToEmpty()
        val oldCpiRates = inflationRepository.cpiDatabaseRates.value?.toMutableList()
        val oldRpiRates = inflationRepository.rpiDatabaseRates.value?.toMutableList()
        // I am making sure that the latest year added to the end of the rates.
        oldCpiRates?.add(DatabaseCpiPct("", "3.0", "",
            "1900","September", "", "", "", ""))
        oldRpiRates?.add(
            DatabaseRpiPct("", "3.0", "",
                Year.now().toString(),"September", "", "", "", "")
        )
        inflationRepository.cpiDatabaseRates.value = oldCpiRates
        inflationRepository.rpiDatabaseRates.value = oldRpiRates
        revaluationViewModel.cpiPercentages = inflationRepository.getSeptemberCpi()
        revaluationViewModel.rpiPercentages = inflationRepository.getSeptemberRpi()

        revaluationViewModel.cpiPercentages.getOrAwaitValue()
        revaluationViewModel.rpiPercentages.getOrAwaitValue()

        // Call function
        revaluationViewModel.validateBeforeCalculation()

        // This checks that a Toast was displayed
        assertThat(revaluationViewModel.toastText.getOrAwaitValue(),
            `is`(not("null"))
        )

        // Test the actual Toast value.
        assertThat(revaluationViewModel.toastTest.getOrAwaitValue(), `is`("CPI and RPI Mismatch"))

    }

    @Test
    fun testLiveData_resultsCalculated_cpiRpiRevalOneGmpRevalCalculated() {
        revaluationViewModel.startDateInfo.value = "01/01/2017"
        revaluationViewModel.endDateInfo.value = "01/01/2020"

        // Call function
        revaluationViewModel.validateBeforeCalculation()

        // This tests that the cpi and rpi revaluation is not 0.0 (a failure), and GMP calculates
        assertThat(revaluationViewModel.revalCalcResults.getOrAwaitValue().cpiHigh, `is`(not(0.0)))
        assertThat(revaluationViewModel.revalCalcResults.getOrAwaitValue().cpiLow, `is`(not(0.0)))
        assertThat(revaluationViewModel.revalCalcResults.getOrAwaitValue().rpiHigh, `is`(not(0.0)))
        assertThat(revaluationViewModel.revalCalcResults.getOrAwaitValue().rpiLow, `is`(not(0.0)))
        assertThat(revaluationViewModel.revalCalcResults.getOrAwaitValue().gmpTaxYears, `is`(1.0973))
        assertThat(revaluationViewModel.revalCalcResults.getOrAwaitValue().gmpSixthAprils, `is`(1.1494))

    }

}