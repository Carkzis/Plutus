package com.carkzis.android.plutus.inflation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.carkzis.android.plutus.CpiPctViewModel
import com.carkzis.android.plutus.data.FakeRepository
import com.carkzis.android.plutus.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CpiPctViewModelTest() {

    private lateinit var cpiPctViewModel: CpiPctViewModel

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
        cpiPctViewModel = CpiPctViewModel(inflationRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun refreshCpiInflationRates_noErrorNonEmpty_getSuccess() {

        cpiPctViewModel.testRefresh()
        // Make sure we then get six items are emitting.
        assertThat(cpiPctViewModel.inflationRates.getOrAwaitValue().size, `is`(6))

        // Check that the status is now done.
        assertThat(cpiPctViewModel.loadingStatus.getOrAwaitValue().name, `is`("DONE"))

    }

    @Test
    fun refreshCpiInflationRates_errorAndNullResults_getLoadingStatusIsError() {

        inflationRepository.setNull(true)
        inflationRepository.setToEmpty()
        // This will set this to the value of the repository.
        cpiPctViewModel.inflationRates = inflationRepository.getCpiPercentages()

        cpiPctViewModel.inflationRates.observeForTesting {

            cpiPctViewModel.testRefresh()

            dispatcher.advanceTimeBy(500)

            assertThat(cpiPctViewModel.loadingStatus.getOrAwaitValue().name, `is`("ERROR"))

            // This checks that a Toast was displayed
            assertThat(cpiPctViewModel.toastText.getOrAwaitValue(),
                `is`(not("null"))
            )
        }
    }

    @Test
    fun refreshCpiInflationRates_errorButCacheNotEmpty_getLoadingStatusIsHas() {

        inflationRepository.setNull(true)
        // This will set this to the value of the repository.
        cpiPctViewModel.inflationRates = inflationRepository.getCpiPercentages()

        cpiPctViewModel.inflationRates.observeForTesting {

            cpiPctViewModel.testRefresh()

            dispatcher.advanceTimeBy(500)

            assertThat(cpiPctViewModel.loadingStatus.getOrAwaitValue().name, `is`("DONE"))

            // This checks that a Toast was displayed
            assertThat(cpiPctViewModel.toastText.getOrAwaitValue(),
                `is`(not("null"))
            )
        }
    }

}