package com.example.android.plutus.pcls

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.plutus.DataBindingIdlingResource
import com.example.android.plutus.MainActivity
import com.example.android.plutus.R
import com.example.android.plutus.monitorActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.containsString
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

        // Click the back button to clear the number input keyboard
        Espresso.pressBack()

        // Click the calculate pcls button
        onView(ViewMatchers.withId(R.id.calculate_pcls_button))
            .perform(ViewActions.click())

        // Confirm we end up with the pcls+residual and full pensions calculations being displayed
        onView(ViewMatchers.withId(R.id.opt2_table))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.opt3_table))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Ensure DC funds are not displaying
        onView(ViewMatchers.withId(R.id.db_dc_result_text))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
        onView(ViewMatchers.withId(R.id.np_dc_result_text))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))

        // Ensure correct titles are applied
        onView(ViewMatchers.withId(R.id.opt2_title))
            .check(ViewAssertions.matches(withText(R.string.opt1_description)))
        onView(ViewMatchers.withId(R.id.opt3_title))
            .check(ViewAssertions.matches(withText(R.string.opt2_description)))

        // Confirm the 2 combined pcls result tables are not there
        onView(ViewMatchers.withId(R.id.opt1_table))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
        onView(ViewMatchers.withId(R.id.opt1b_table))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
    }

    @Test
    fun calculationResults_smallDcFundProvided_displayedInUi() = runBlockingTest {
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

        // Click the back button to clear the number input keyboard
        Espresso.pressBack()

        // Click the calculate pcls button
        onView(ViewMatchers.withId(R.id.calculate_pcls_button))
            .perform(ViewActions.click())

        // Confirm we end up with the pcls calculation results being displayed
        onView(ViewMatchers.withId(R.id.opt2_table))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.opt3_table))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Ensure DC funds are displayed in the DB and full pension tables
        onView(ViewMatchers.withId(R.id.db_dc_result_text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.np_dc_result_text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Confirm we end up with the combined pcls calculation results being displayed
        onView(ViewMatchers.withId(R.id.opt1_table))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Ensure correct titles are applied
        onView(ViewMatchers.withId(R.id.opt1_title))
            .check(ViewAssertions.matches(withText(R.string.opt1_description)))
        onView(ViewMatchers.withId(R.id.opt2_title))
            .check(ViewAssertions.matches(withText(R.string.opt2_description)))
        onView(ViewMatchers.withId(R.id.opt3_title))
            .check(ViewAssertions.matches(withText(R.string.opt3_description)))

        // Confirm option for taking residual dc as a UFPLS is not displayed
        onView(ViewMatchers.withId(R.id.opt1b_table))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
    }

    @Test
    fun calculationResults_largeDcFundProvided_displayedInUi() = runBlockingTest {
        // Start on contents screen, as DataBindingIdlingResource.monitorFragment
        // currently not functioning correctly
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)
        // Click on the pcls calculator button to go to pcls calculator
        onView(ViewMatchers.withId(R.id.pcls_button))
            .perform(ViewActions.click())

        // Click the pension edit text, and enter "1500.00"
        onView(ViewMatchers.withId(R.id.pension_edittext))
            .perform(typeText("5,000.00"))
        // Click on the commutation edit text, and enter 15.0
        onView(ViewMatchers.withId(R.id.commutation_factor_edittext))
            .perform(typeText("22.0"))
        // Click on the commutation edit text, and enter 15.0
        onView(ViewMatchers.withId(R.id.dc_edittext))
            .perform(typeText("95,000.00"))

        // Click the back button to clear the number input keyboard
        Espresso.pressBack()

        // Click the calculate pcls button
        onView(ViewMatchers.withId(R.id.calculate_pcls_button))
            .perform(ViewActions.click())

        // Confirm we end up with the both combined pcls calculation results being displayed
        onView(ViewMatchers.withId(R.id.opt1_table))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.opt1b_table))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Ensure correct titles are applied, option 1b always has the same title so not checked
        onView(ViewMatchers.withId(R.id.opt1_title))
            .check(ViewAssertions.matches(withText(R.string.opt1a_description)))
        onView(ViewMatchers.withId(R.id.opt2_title))
            .check(ViewAssertions.matches(withText(R.string.opt2_description)))

        // Confirm we end up with the pcls calculation results being displayed
        // Note: Need to scroll to the bottom as it will likely go off page
        onView(ViewMatchers.withId(R.id.opt2_table))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.opt3_table))
            .perform(scrollTo())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Check option 3 has the correct title
        onView(ViewMatchers.withId(R.id.opt3_title))
            .check(ViewAssertions.matches(withText(R.string.opt3_description)))

        // Ensure DC funds are displayed in the DB and full pension tables
        onView(ViewMatchers.withId(R.id.db_dc_result_text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.np_dc_result_text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

}