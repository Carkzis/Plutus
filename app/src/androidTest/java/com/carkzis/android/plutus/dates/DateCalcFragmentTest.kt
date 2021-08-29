package com.carkzis.android.plutus.dates

import android.widget.DatePicker
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.carkzis.android.plutus.DataBindingIdlingResource
import com.carkzis.android.plutus.MainActivity
import com.carkzis.android.plutus.R
import com.carkzis.android.plutus.monitorActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class DateCalcFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    private val dataBindingIdlingResource = DataBindingIdlingResource()
    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @After
    fun closeActivityScenerio() = runBlockingTest {
        activityScenario.close()
    }

    @Test
    fun defaultDateResults_showInUi() = runBlockingTest {
        // Start on contents screen, as DataBindingIdlingResource.monitorFragment
        // currently not functioning correctly
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the date calculator button to go to date calculator
        onView(withId(R.id.date_button))
            .perform(click())

        onView(withId(R.id.years_result_text))
            .check(matches(withSubstring("0 ")))
        onView(withId(R.id.months_result_text))
            .check(matches(withSubstring("0 ")))
        onView(withId(R.id.weeks_result_text))
            .check(matches(withSubstring("0 ")))
        onView(withId(R.id.days_result_text))
            .check(matches(withSubstring("0 ")))
        onView(withId(R.id.years_months_result_text))
            .check(matches(withSubstring("0 ")))
        onView(withId(R.id.years_months_result_text))
            .check(matches(withSubstring(" 0 ")))
        onView(withId(R.id.years_days_result_text))
            .check(matches(withSubstring("0 ")))
        onView(withId(R.id.years_days_result_text))
            .check(matches(withSubstring(" 0 ")))
        onView(withId(R.id.tax_years_result_text))
            .check(matches(withSubstring("0 ")))
        onView(withId(R.id.sixth_aprils_result_text))
            .check(matches(withSubstring("0 ")))

    }

    @Test
    fun dateCalcResults_inputStartAndEndDate_showInUi() = runBlockingTest {

        // Start on contents screen, as DataBindingIdlingResource.monitorFragment
        // currently not functioning correctly
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the date calculator button to go to date calculator
        onView(withId(R.id.date_button))
            .perform(click())

        // Click on the start date edit text to open calendar.
        onView(withId(R.id.start_date_edittext))
            .perform(click())
        // Open date picker for start date and input date
        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(2000, 2, 22))
        onView(withText(R.string.ok)).perform(click())

        // Click on the end date edit text to open calendar.
        onView(withId(R.id.end_date_edittext))
            .perform(click())
        // Open date picker for start date and input date
        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(2021, 5, 1))
        onView(withText(R.string.ok)).perform(click())

        // Click calculate button
        onView(withId(R.id.date_calc_button))
            .perform(click())

        onView(withId(R.id.years_result_text))
            .check(matches(withSubstring("21 ")))
        onView(withId(R.id.months_result_text))
            .check(matches(withSubstring("254 ")))
        onView(withId(R.id.weeks_result_text))
            .check(matches(withSubstring("1105 ")))
        onView(withId(R.id.days_result_text))
            .check(matches(withSubstring("7740 ")))
        onView(withId(R.id.years_months_result_text))
            .check(matches(withSubstring("21 ")))
        onView(withId(R.id.years_months_result_text))
            .check(matches(withSubstring(" 2 ")))
        onView(withId(R.id.years_days_result_text))
            .check(matches(withSubstring("21 ")))
        onView(withId(R.id.years_days_result_text))
            .check(matches(withSubstring(" 69 ")))
        onView(withId(R.id.tax_years_result_text))
            .check(matches(withSubstring("21 ")))
        onView(withId(R.id.sixth_aprils_result_text))
            .check(matches(withSubstring("22 ")))

    }

    @Test
    fun dateCalcResults_openDialogAndPressBack_dialogDisappears() = runBlockingTest {

        // Start on contents screen, as DataBindingIdlingResource.monitorFragment
        // currently not functioning correctly
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the date calculator button to go to date calculator
        onView(withId(R.id.date_button))
            .perform(click())

        // Click on the start date edit text to open calendar.
        onView(withId(R.id.start_date_edittext))
            .perform(click())

        onView(withText(R.string.ok)).check(matches(isDisplayed()))

        Espresso.pressBack()

        onView(withText(R.string.ok)).check(doesNotExist())

    }

}