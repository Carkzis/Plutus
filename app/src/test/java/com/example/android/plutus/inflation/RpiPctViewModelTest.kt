package com.example.android.plutus.inflation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.plutus.data.FakeRepository
import com.example.android.plutus.getOrAwaitValue
import com.example.android.plutus.observeForTesting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RpiPctViewModelTest() {

private lateinit var rpiPctViewModel: RpiPctViewModel

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
    rpiPctViewModel = RpiPctViewModel(inflationRepository)
}

@After
fun tearDown() {
    Dispatchers.resetMain()
}

@Test
fun refreshRpiInflationRates_noErrorNonEmpty_getSuccess() {

    rpiPctViewModel.testRefresh()
    // Make sure we then get six items are emitting.
    MatcherAssert.assertThat(
        rpiPctViewModel.inflationRates.getOrAwaitValue().size,
        CoreMatchers.`is`(16)
    )

    // Check that the status is now done.
    MatcherAssert.assertThat(
        rpiPctViewModel.loadingStatus.getOrAwaitValue().name,
        CoreMatchers.`is`("DONE")
    )

}

@Test
fun refreshRpiInflationRates_errorAndNullResults_getLoadingStatusIsError() {

    inflationRepository.setNull(true)
    inflationRepository.setToEmpty()
    // This will set this to the value of the repository.
    rpiPctViewModel.inflationRates = inflationRepository.getRpiPercentages()

    rpiPctViewModel.inflationRates.observeForTesting {

        rpiPctViewModel.testRefresh()

        dispatcher.advanceTimeBy(500)

        MatcherAssert.assertThat(
            rpiPctViewModel.loadingStatus.getOrAwaitValue().name,
            CoreMatchers.`is`("ERROR")
        )

        // This checks that a Toast was displayed
        MatcherAssert.assertThat(
            rpiPctViewModel.toastText.getOrAwaitValue(),
            CoreMatchers.`is`(CoreMatchers.not("null"))
        )
    }
}

@Test
fun refreshRpiInflationRates_errorButCacheNotEmpty_getLoadingStatusIsHas() {

    inflationRepository.setNull(true)
    // This will set this to the value of the repository.
    rpiPctViewModel.inflationRates = inflationRepository.getRpiPercentages()

    rpiPctViewModel.inflationRates.observeForTesting {

        rpiPctViewModel.testRefresh()

        dispatcher.advanceTimeBy(500)

        MatcherAssert.assertThat(
            rpiPctViewModel.loadingStatus.getOrAwaitValue().name,
            CoreMatchers.`is`("DONE")
        )

        // This checks that a Toast was displayed
        MatcherAssert.assertThat(
            rpiPctViewModel.toastText.getOrAwaitValue(),
            CoreMatchers.`is`(CoreMatchers.not("null"))
        )
    }
}

}