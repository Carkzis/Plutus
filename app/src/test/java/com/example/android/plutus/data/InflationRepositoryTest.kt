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
    fun refreshCpiInflation_error_dataNotRefreshed() = runBlockingTest {
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
    fun refreshCpiInflation_noError_refreshedWithNewData() = runBlockingTest {
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
    fun getCpiRates_convertDatabaseCpiInflationRateToInflationRate_returnsLiveDataListOfSameSize()
            = runBlockingTest {

        // Call the refresh method
        inflationRepository.refreshCpiPercentages()

        assertThat(inflationRepository.cpiDatabaseRates.getOrAwaitValue().size, `is`(6))

        val convertedLiveData = inflationRepository.getCpiPercentages()

        assertThat(convertedLiveData.getOrAwaitValue().size, `is`(6))
    }

    @Test
    fun refreshRpiInflation_error_dataNotRefreshed() = runBlockingTest {
        // The initial size should be 5

        assertThat(inflationRepository.rpiDatabaseRates.getOrAwaitValue().size, `is`(15))

        // Replicate an error occuring
        inflationRepository.setReturnError(true)

        // Call the refresh method
        inflationRepository.refreshRpiPercentages()

        // The size should not change, as the error should prevent the refresh function progressing.
        assertThat(inflationRepository.rpiDatabaseRates.getOrAwaitValue().size, `is`(15))
    }

    @Test
    fun refreshRpiInflation_noError_refreshedWithNewData() = runBlockingTest {
        // The initial size should be 5

        assertThat(inflationRepository.rpiDatabaseRates.getOrAwaitValue().size, `is`(15))

        // Replicate an error occuring
        inflationRepository.setReturnError(false)

        // Call the refresh method
        inflationRepository.refreshRpiPercentages()

        // The size should not change, as the error should prevent the refresh function progressing.
        assertThat(inflationRepository.rpiDatabaseRates.getOrAwaitValue().size, `is`(16))
    }

    @Test
    fun getRpiRates_convertDatabaseRpiInflationRateToInflationRate_returnsLiveDataListOfSameSize()
            = runBlockingTest {

        // Call the refresh method
        inflationRepository.refreshRpiPercentages()

        assertThat(inflationRepository.rpiDatabaseRates.getOrAwaitValue().size, `is`(16))

        val convertedLiveData = inflationRepository.getRpiPercentages()

        assertThat(convertedLiveData.getOrAwaitValue().size, `is`(16))
    }

    @Test
    fun refreshCpiItems_error_dataNotRefreshed() = runBlockingTest {
        // The initial size should be 5

        assertThat(inflationRepository.cpiDatabaseItems.getOrAwaitValue().size, `is`(10))

        // Replicate an error occuring
        inflationRepository.setReturnError(true)

        // Call the refresh method
        inflationRepository.refreshCpiItems()

        // The size should not change, as the error should prevent the refresh function progressing.
        assertThat(inflationRepository.cpiDatabaseItems.getOrAwaitValue().size, `is`(10))
    }

    @Test
    fun refreshCpiItems_noError_refreshedWithNewData() = runBlockingTest {
        // The initial size should be 5

        assertThat(inflationRepository.cpiDatabaseItems.getOrAwaitValue().size, `is`(10))

        // Replicate an error occuring
        inflationRepository.setReturnError(false)

        // Call the refresh method
        inflationRepository.refreshCpiItems()

        // The size should not change, as the error should prevent the refresh function progressing.
        assertThat(inflationRepository.cpiDatabaseItems.getOrAwaitValue().size, `is`(11))
    }

    @Test
    fun getCpiItems_convertDatabaseCpiInflationRateToInflationRate_returnsLiveDataListOfSameSize()
            = runBlockingTest {

        // Call the refresh method
        inflationRepository.refreshCpiItems()

        assertThat(inflationRepository.cpiDatabaseItems.getOrAwaitValue().size, `is`(11))

        val convertedLiveData = inflationRepository.getCpiItems()

        assertThat(convertedLiveData.getOrAwaitValue().size, `is`(11))
    }

    @Test
    fun refreshRpiItems_error_dataNotRefreshed() = runBlockingTest {
        // The initial size should be 5

        assertThat(inflationRepository.rpiDatabaseItems.getOrAwaitValue().size, `is`(30))

        // Replicate an error occuring
        inflationRepository.setReturnError(true)

        // Call the refresh method
        inflationRepository.refreshRpiItems()

        // The size should not change, as the error should prevent the refresh function progressing.
        assertThat(inflationRepository.rpiDatabaseItems.getOrAwaitValue().size, `is`(30))
    }

    @Test
    fun refreshRpiItems_noError_refreshedWithNewData() = runBlockingTest {
        // The initial size should be 5

        assertThat(inflationRepository.rpiDatabaseItems.getOrAwaitValue().size, `is`(30))

        // Replicate an error occuring
        inflationRepository.setReturnError(false)

        // Call the refresh method
        inflationRepository.refreshRpiItems()

        // The size should not change, as the error should prevent the refresh function progressing.
        assertThat(inflationRepository.rpiDatabaseItems.getOrAwaitValue().size, `is`(31))
    }

    @Test
    fun getRpiItems_convertDatabaseCpiInflationRateToInflationRate_returnsLiveDataListOfSameSize()
            = runBlockingTest {

        // Call the refresh method
        inflationRepository.refreshRpiItems()

        assertThat(inflationRepository.rpiDatabaseItems.getOrAwaitValue().size, `is`(31))

        val convertedLiveData = inflationRepository.getRpiItems()

        assertThat(convertedLiveData.getOrAwaitValue().size, `is`(31))
    }


}