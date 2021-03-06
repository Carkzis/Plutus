package com.carkzis.android.plutus.inflation

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.carkzis.android.plutus.DataBindingIdlingResource
import com.carkzis.android.plutus.MainActivity
import com.carkzis.android.plutus.R
import com.carkzis.android.plutus.data.Repository
import com.carkzis.android.plutus.monitorActivity
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions
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
class RpiPctFragmentTest {

    // TODO: Test searching capability.

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
    fun rpiInflationResults_whenRequestSuccessful_displayRpiInflationRates() = runBlockingTest {

        // Start on contents screen, as DataBindingIdlingResource.monitorFragment
        // currently not functioning correctly
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click the Inflation Information
        Espresso.onView(ViewMatchers.withId(R.id.inflation_button)).perform(ViewActions.click())
        // Click the CPI button
        Espresso.onView(ViewMatchers.withId(R.id.rpi_button)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.rpi_progress_bar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        BaristaSleepInteractions.sleep(2000)

        // January will always be the first entry, so if it is displayed, the results are displayed.
        Espresso.onView(ViewMatchers.withText("January"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        // Make sure that the progress bar finishes.
        BaristaVisibilityAssertions.assertNotDisplayed(R.id.rpi_progress_bar)

    }

    /**
     * This needs to be run in airplane mode on the emulator and uncommented, which will then
     * display an error message where there are no items in the Cache.
     */
//    @Test
//    fun rpiInflationResults_whenNoConnectionNoDataInCache_displayErrorMessage() = runBlockingTest {
//
//        // Start on contents screen, as DataBindingIdlingResource.monitorFragment
//        // currently not functioning correctly
//        activityScenario = ActivityScenario.launch(MainActivity::class.java)
//        dataBindingIdlingResource.monitorActivity(activityScenario)
//
//        // Click the Inflation Information
//        onView(withId(R.id.inflation_button)).perform(click())
//        // Click the CPI button
//        onView(withId(R.id.rpi_button)).perform(click())
//
//        // This shouldn't be displaying.
//        assertNotExist("January")
//
//        // Ensure the CPI error is showing.
//        onView(withId(R.id.rpi_error)).check(matches(isDisplayed()))
//
//        // Ensure the progress bar is no longer showing.
//        assertNotDisplayed(R.id.rpi_progress_bar)
//
//    }

}