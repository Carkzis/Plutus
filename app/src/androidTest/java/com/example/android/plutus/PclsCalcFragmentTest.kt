package com.example.android.plutus

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class PclsCalcFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Test
    fun calculationResults_noDcFund_displayedInUi() = runBlockingTest {
        // Start on contents screen, as DataBindingIdlingResource.monitorFragment
        // currently not functioning correctly
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)
        // Click on the pcls calculator button to go to pcls calculator
        onView(ViewMatchers.withId(R.id.pcls_button))
            .perform(ViewActions.click())

        // Click the pension edit text, and enter "1500.00"
        onView(ViewMatchers.withId(R.id.pension_edittext))
            .perform(typeText("1500.00"))
        // Click on the commutation edit text, and enter 15.0
        onView(ViewMatchers.withId(R.id.commutation_factor_edittext))
            .perform(typeText("15.0"))

        // Click the calculate pcls button
        onView(ViewMatchers.withId(R.id.calculate_pcls_button))
            .perform(ViewActions.click())

        // Confirm we end up with the pcls+residual and full pensions calculations being displayed
        onView(ViewMatchers.withId(R.id.db_only_linear_layout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.no_pcls_linear_layout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Ensure DC funds are not displaying
        onView(ViewMatchers.withId(R.id.db_dc_result_text))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
        onView(ViewMatchers.withId(R.id.np_dc_result_text))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))

        // Confirm the combined pcls linear layout is not there
        onView(ViewMatchers.withId(R.id.combined_linear_layout))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
    }

    @Test
    fun calculationResults_DcFundProvided_displayedInUi() = runBlockingTest {
        // Start on contents screen, as DataBindingIdlingResource.monitorFragment
        // currently not functioning correctly
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)
        // Click on the pcls calculator button to go to pcls calculator
        onView(ViewMatchers.withId(R.id.pcls_button))
            .perform(ViewActions.click())

        // Click the pension edit text, and enter "1500.00"
        onView(ViewMatchers.withId(R.id.pension_edittext))
            .perform(typeText("20000.00"))
        // Click on the commutation edit text, and enter 15.0
        onView(ViewMatchers.withId(R.id.commutation_factor_edittext))
            .perform(typeText("25.0"))
        // Click on the commutation edit text, and enter 15.0
        onView(ViewMatchers.withId(R.id.dc_edittext))
            .perform(typeText("25.0"))

        // Click the calculate pcls button
        onView(ViewMatchers.withId(R.id.calculate_pcls_button))
            .perform(ViewActions.click())

        // Confirm we end up with the pcls calculation results being displayed
        onView(ViewMatchers.withId(R.id.db_only_linear_layout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.no_pcls_linear_layout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Ensure DC funds are displayed in the DB and full pension linear layouts
        onView(ViewMatchers.withId(R.id.db_dc_result_text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.np_dc_result_text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Confirm we end up with the combined pcls calculation results being displayed
        onView(ViewMatchers.withId(R.id.combined_linear_layout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}