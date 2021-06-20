package com.example.android.plutus.inflation

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.plutus.DataBindingIdlingResource
import com.example.android.plutus.MainActivity
import com.example.android.plutus.R
import com.example.android.plutus.data.Repository
import com.example.android.plutus.monitorActivity
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleep
import com.schibsted.spain.barista.rule.cleardata.ClearDatabaseRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class CpiInflationFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var clearDatabaseRule = ClearDatabaseRule()

    @Inject
    lateinit var repository: Repository

    private val dataBindingIdlingResource = DataBindingIdlingResource()
    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @Before
    fun init() {
        hiltRule.inject()
    }

    @After
    fun closeActivityScenerio() = runBlockingTest {
        activityScenario.close()
    }

    /**
     * This needs to be run with internet and uncommented.
     */
    @Test
    fun cpiInflationResults_whenRequestSuccessful_displayCpiInflationRates() = runBlockingTest {

        // Start on contents screen, as DataBindingIdlingResource.monitorFragment
        // currently not functioning correctly
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click the Inflation Information
        onView(withId(R.id.inflation_button)).perform(click())
        // Click the CPI button
        onView(withId(R.id.cpi_button)).perform(click())

        onView(withId(R.id.cpi_progress_bar)).check(matches(isDisplayed()))

        sleep(2000)

        // January will always be the first entry, so if it is displayed, the results are displayed.
        onView(withText("January")).check(matches(isDisplayed()))
        // Make sure that the progress bar finishes.
        assertNotDisplayed(R.id.cpi_progress_bar)

    }

    /**
     * This needs to be run in airplane mode on the emulator and uncommented, which will then
     * display an error message where there are no items in the Cache.
     */
//    @Test
//    fun cpiInflationResults_whenNoConnectionNoDataInCache_displayErrorMessage() = runBlockingTest {
//
//        // Start on contents screen, as DataBindingIdlingResource.monitorFragment
//        // currently not functioning correctly
//        activityScenario = ActivityScenario.launch(MainActivity::class.java)
//        dataBindingIdlingResource.monitorActivity(activityScenario)
//
//        // Click the Inflation Information
//        onView(withId(R.id.inflation_button)).perform(click())
//        // Click the CPI button
//        onView(withId(R.id.cpi_button)).perform(click())
//
//        // This shouldn't be displaying.
//        assertNotExist("January")
//
//        // Ensure the CPI error is showing.
//        onView(withId(R.id.cpi_error)).check(matches(isDisplayed()))
//
//        // Ensure the progress bar is no longer showing.
//        assertNotDisplayed(R.id.cpi_progress_bar)
//
//    }

}