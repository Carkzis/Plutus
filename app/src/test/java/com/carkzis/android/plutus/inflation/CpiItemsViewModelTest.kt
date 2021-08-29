package com.carkzis.android.plutus.inflation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.carkzis.android.plutus.data.FakeRepository
import com.carkzis.android.plutus.getOrAwaitValue
import com.carkzis.android.plutus.observeForTesting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CpiItemsViewModelTest() {

    private lateinit var cpiItemsViewModel: CpiItemsViewModel

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
        cpiItemsViewModel = CpiItemsViewModel(inflationRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun refreshCpiInflationRates_noErrorNonEmpty_getSuccess() {

        cpiItemsViewModel.testRefresh()
        // Make sure we then get six items are emitting.
        MatcherAssert.assertThat(
            cpiItemsViewModel.inflationRates.getOrAwaitValue().size,
            CoreMatchers.`is`(11)
        )

        // Check that the status is now done.
        MatcherAssert.assertThat(
            cpiItemsViewModel.loadingStatus.getOrAwaitValue().name,
            CoreMatchers.`is`("DONE")
        )

    }

    @Test
    fun refreshCpiInflationRates_errorAndNullResults_getLoadingStatusIsError() {

        inflationRepository.setNull(true)
        inflationRepository.setToEmpty()
        // This will set this to the value of the repository.
        cpiItemsViewModel.inflationRates = inflationRepository.getCpiItems()

        cpiItemsViewModel.inflationRates.observeForTesting {

            cpiItemsViewModel.testRefresh()

            dispatcher.advanceTimeBy(500)

            MatcherAssert.assertThat(
                cpiItemsViewModel.loadingStatus.getOrAwaitValue().name,
                CoreMatchers.`is`("ERROR")
            )

            // This checks that a Toast was displayed
            MatcherAssert.assertThat(
                cpiItemsViewModel.toastText.getOrAwaitValue(),
                CoreMatchers.`is`(CoreMatchers.not("null"))
            )
        }
    }

    @Test
    fun refreshCpiInflationRates_errorButCacheNotEmpty_getLoadingStatusIsHas() {

        inflationRepository.setNull(true)
        // This will set this to the value of the repository.
        cpiItemsViewModel.inflationRates = inflationRepository.getCpiItems()

        cpiItemsViewModel.inflationRates.observeForTesting {

            cpiItemsViewModel.testRefresh()

            dispatcher.advanceTimeBy(500)

            MatcherAssert.assertThat(
                cpiItemsViewModel.loadingStatus.getOrAwaitValue().name,
                CoreMatchers.`is`("DONE")
            )

            // This checks that a Toast was displayed
            MatcherAssert.assertThat(
                cpiItemsViewModel.toastText.getOrAwaitValue(),
                CoreMatchers.`is`(CoreMatchers.not("null"))
            )
        }
    }

}