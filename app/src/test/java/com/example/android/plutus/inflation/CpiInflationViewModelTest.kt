package com.example.android.plutus.inflation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.plutus.CpiInflationViewModel
import com.example.android.plutus.InflationRate
import com.example.android.plutus.data.DatabaseCpiInflationRate
import com.example.android.plutus.data.FakeRepository
import com.example.android.plutus.getOrAwaitValue
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CpiInflationViewModelTest() {

    private lateinit var cpiInflationViewModel: CpiInflationViewModel

    private lateinit var inflationRepository: FakeRepository

    // This is from the kotlin docs, to allow access to Dispatcher.Main in testing.
    private val dispatcher = TestCoroutineDispatcher()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        inflationRepository = FakeRepository()
        cpiInflationViewModel = CpiInflationViewModel(inflationRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun refreshCpiInflationRates_noErrorNonEmpty_getSuccess() = runBlocking {

        // Make sure we then get six items are emitting.
        assertThat(cpiInflationViewModel.inflationRates.getOrAwaitValue().size, `is`(6))

        // Check that the status is now done.
        assertThat(cpiInflationViewModel.loadingStatus.getOrAwaitValue().name, `is`("DONE"))
    }

    @Test
    fun refreshCpiInflationRates_emptyResults_getLoadingStatusIsDone() = runBlocking {

        inflationRepository.setNull(true)
        cpiInflationViewModel.testRefresh()
        cpiInflationViewModel.inflationRates = inflationRepository.getRates("cpi")

        // Check that the status is now done.
        assertThat(cpiInflationViewModel.loadingStatus.getOrAwaitValue().name, `is`("ERROR"))

        assertThat(cpiInflationViewModel.toastText.getOrAwaitValue(),
            `is`(not("null"))
        )
    }

}