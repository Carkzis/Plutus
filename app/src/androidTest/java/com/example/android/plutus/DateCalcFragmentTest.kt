package com.example.android.plutus

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class DateCalcFragmentTest {

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Test
    fun defaultDateResults_showInUi() = runBlockingTest {
        // Start on contents screen, as DataBindingIdlingResource.monitorFragment
        // currently not functioning correctly
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the date calculator button to go to date calculator
        Espresso.onView(ViewMatchers.withId(R.id.date_button))
            .perform(ViewActions.click())

        onView(ViewMatchers.withId(R.id.years_result_text))
            .check(ViewAssertions.matches(withSubstring("0 ")))
        onView(ViewMatchers.withId(R.id.months_result_text))
            .check(ViewAssertions.matches(withSubstring("0 ")))
        onView(ViewMatchers.withId(R.id.weeks_result_text))
            .check(ViewAssertions.matches(withSubstring("0 ")))
        onView(ViewMatchers.withId(R.id.days_result_text))
            .check(ViewAssertions.matches(withSubstring("0 ")))
        onView(ViewMatchers.withId(R.id.years_months_result_text))
            .check(ViewAssertions.matches(withSubstring("0 ")))
        onView(ViewMatchers.withId(R.id.years_months_result_text))
            .check(ViewAssertions.matches(withSubstring(" 0 ")))
        onView(ViewMatchers.withId(R.id.years_days_result_text))
            .check(ViewAssertions.matches(withSubstring("0 ")))
        onView(ViewMatchers.withId(R.id.years_days_result_text))
            .check(ViewAssertions.matches(withSubstring(" 0 ")))
        onView(ViewMatchers.withId(R.id.tax_years_result_text))
            .check(ViewAssertions.matches(withSubstring("0 ")))
        onView(ViewMatchers.withId(R.id.sixth_aprils_result_text))
            .check(ViewAssertions.matches(withSubstring("0 ")))
    }

    // TODO: Can add inputs and get results

    // TODO: Can try to add inputs but then cancel

}