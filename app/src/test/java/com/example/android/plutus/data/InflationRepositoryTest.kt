package com.example.android.plutus.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.plutus.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class InflationRepositoryTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Use FakeRepository that will imitate data having been received or not.
    private lateinit var inflationRepository: FakeRepository

    @Before
    fun createRepository() {
        inflationRepository = FakeRepository()
    }

    @Test
    fun refreshInflation_error_dataNotRefreshed() = runBlockingTest {
        // The initial size should be 5

        assertThat(inflationRepository.cpiDatabaseRates.getOrAwaitValue().size, `is`(5))

        // Replicate an error occuring
        inflationRepository.setReturnError(true)

        // Call the refresh method
        inflationRepository.refreshCpiPercentages()

        // The size should not change, as the error should prevent the refresh function progressing.
        assertThat(inflationRepository.cpiDatabaseRates.getOrAwaitValue().size, `is`(5))
    }

    @Test
    fun refreshInflation_noError_refreshedWithNewData() = runBlockingTest {
        // The initial size should be 5

        assertThat(inflationRepository.cpiDatabaseRates.getOrAwaitValue().size, `is`(5))

        // Replicate an error occuring
        inflationRepository.setReturnError(false)

        // Call the refresh method
        inflationRepository.refreshCpiPercentages()

        // The size should not change, as the error should prevent the refresh function progressing.
        assertThat(inflationRepository.cpiDatabaseRates.getOrAwaitValue().size, `is`(6))
    }

    @Test
    fun getRates_convertDatabaseCpiInflationRateToInflationRate_returnsLiveDataListOfSameSize()
            = runBlockingTest {

        // Call the refresh method
        inflationRepository.refreshCpiPercentages()

        assertThat(inflationRepository.cpiDatabaseRates.getOrAwaitValue().size, `is`(6))

        val convertedLiveData = inflationRepository.getCpiPercentages()

        assertThat(convertedLiveData.getOrAwaitValue().size, `is`(6))
    }

}