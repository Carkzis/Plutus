package com.example.android.plutus.inflation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.plutus.*
import com.example.android.plutus.data.FakeRepository
import kotlinx.coroutines.*
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
    fun refreshCpiInflationRates_noErrorNonEmpty_getSuccess() {

        cpiInflationViewModel.testRefresh()
        // Make sure we then get six items are emitting.
        assertThat(cpiInflationViewModel.inflationRates.getOrAwaitValue().size, `is`(6))

        // Check that the status is now done.
        assertThat(cpiInflationViewModel.loadingStatus.getOrAwaitValue().name, `is`("DONE"))

    }

    @Test
    fun refreshCpiInflationRates_errorAndNullResults_getLoadingStatusIsError() {

        inflationRepository.setNull(true)
        inflationRepository.setToEmpty()
        // This will set this to the value of the repository.
        cpiInflationViewModel.inflationRates = inflationRepository.getCpiRates("cpi")

        cpiInflationViewModel.inflationRates.observeForTesting {

            cpiInflationViewModel.testRefresh()

            assertThat(cpiInflationViewModel.loadingStatus.getOrAwaitValue().name, `is`("ERROR"))

            // This checks that a Toast was displayed
            assertThat(cpiInflationViewModel.toastText.getOrAwaitValue(),
                `is`(not("null"))
            )
        }
    }

    @Test
    fun refreshCpiInflationRates_errorButCacheNotEmpty_getLoadingStatusIsHas() {

        inflationRepository.setNull(true)
        // This will set this to the value of the repository.
        cpiInflationViewModel.inflationRates = inflationRepository.getCpiRates("cpi")

        cpiInflationViewModel.inflationRates.observeForTesting {

            cpiInflationViewModel.testRefresh()

            assertThat(cpiInflationViewModel.loadingStatus.getOrAwaitValue().name, `is`("DONE"))

            // This checks that a Toast was displayed
            assertThat(cpiInflationViewModel.toastText.getOrAwaitValue(),
                `is`(not("null"))
            )
        }
    }

}