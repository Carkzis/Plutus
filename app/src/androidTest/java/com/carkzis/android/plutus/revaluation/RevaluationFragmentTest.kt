package com.carkzis.android.plutus.revaluation

import android.widget.DatePicker
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.PickerActions
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
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class RevaluationFragmentTest() {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var clearDatabaseRule = ClearDatabaseRule()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Inject
    lateinit var repository: Repository

    private val dataBindingIdlingResource = DataBindingIdlingResource()
    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @After
    fun closeActivityScenerio() = runBlockingTest {
        activityScenario.close()
    }

    /**
     * This needs to be run with internet and uncommented.
     */
    @Test
    fun revaluationCalculationResults_whenDataRefreshAttempted_displayResults() = runBlockingTest {

        // Start on contents screen, as DataBindingIdlingResource.monitorFragment
        // currently not functioning correctly
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click the Inflation Information
        Espresso.onView(ViewMatchers.withId(R.id.revaluation_button)).perform(ViewActions.click())

        // Click on the start date edit text to open calendar.
        Espresso.onView(ViewMatchers.withId(R.id.reval_start_edittext))
            .perform(ViewActions.click())
        // Open date picker for start date and input date
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(2000, 2, 22))
        Espresso.onView(ViewMatchers.withText(R.string.ok)).perform(ViewActions.click())

        // Click on the end date edit text to open calendar.
        Espresso.onView(ViewMatchers.withId(R.id.reval_end_edittext))
            .perform(ViewActions.click())
        // Open date picker for start date and input date
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(2021, 5, 1))
        Espresso.onView(ViewMatchers.withText(R.string.ok)).perform(ViewActions.click())

        // Click calculate button
        Espresso.onView(ViewMatchers.withId(R.id.calculate_reval_button))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.reval_progress_bar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Await refresh of data.
        BaristaSleepInteractions.sleep(2000)

        // Attempt calculation again.
        Espresso.onView(ViewMatchers.withId(R.id.calculate_reval_button))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.cpi_high_text))
            .check(ViewAssertions.matches(ViewMatchers.withSubstring("1.624")))
        Espresso.onView(ViewMatchers.withId(R.id.cpi_low_text))
            .check(ViewAssertions.matches(ViewMatchers.withSubstring("1.236")))
        Espresso.onView(ViewMatchers.withId(R.id.rpi_high_text))
            .check(ViewAssertions.matches(ViewMatchers.withSubstring("1.771")))
        Espresso.onView(ViewMatchers.withId(R.id.rpi_low_text))
            .check(ViewAssertions.matches(ViewMatchers.withSubstring("1.345")))
        Espresso.onView(ViewMatchers.withId(R.id.gmpf_tax_years_text))
            .check(ViewAssertions.matches(ViewMatchers.withSubstring("3.572")))
        Espresso.onView(ViewMatchers.withId(R.id.gmpf_sixth_aprils_text))
            .check(ViewAssertions.matches(ViewMatchers.withSubstring("3.7952")))

        // Make sure that the progress bar finishes.
        BaristaVisibilityAssertions.assertNotDisplayed(R.id.reval_progress_bar)
    }

    /**
     * This needs to be run in airplane mode on the emulator and uncommented, which will then
     * display an error message where there are no items in the Cache.
     */
//    @Test
//    fun revaluationCalculationResults_whenNoConnectionNoCache_noResultsDisplayed() = runBlockingTest {
//
//        // Start on contents screen, as DataBindingIdlingResource.monitorFragment
//        // currently not functioning correctly
//        activityScenario = ActivityScenario.launch(MainActivity::class.java)
//        dataBindingIdlingResource.monitorActivity(activityScenario)
//
//        // Click the Inflation Information
//        Espresso.onView(ViewMatchers.withId(R.id.revaluation_button)).perform(ViewActions.click())
//
//        BaristaSleepInteractions.sleep(2000)
//
//        // Click on the start date edit text to open calendar.
//        Espresso.onView(ViewMatchers.withId(R.id.reval_start_edittext))
//            .perform(ViewActions.click())
//        // Open date picker for start date and input date
//        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker::class.java.name)))
//            .perform(PickerActions.setDate(2000, 2, 22))
//        Espresso.onView(ViewMatchers.withText(R.string.ok)).perform(ViewActions.click())
//
//        // Click on the end date edit text to open calendar.
//        Espresso.onView(ViewMatchers.withId(R.id.reval_end_edittext))
//            .perform(ViewActions.click())
//        // Open date picker for start date and input date
//        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker::class.java.name)))
//            .perform(PickerActions.setDate(2021, 5, 1))
//        Espresso.onView(ViewMatchers.withText(R.string.ok)).perform(ViewActions.click())
//
//        // Click calculate button
//        Espresso.onView(ViewMatchers.withId(R.id.calculate_reval_button))
//            .perform(ViewActions.click())
//
//        Espresso.onView(ViewMatchers.withId(R.id.cpi_high_text))
//            .check(ViewAssertions.matches(ViewMatchers.withSubstring("0.0")))
//        Espresso.onView(ViewMatchers.withId(R.id.cpi_low_text))
//            .check(ViewAssertions.matches(ViewMatchers.withSubstring("0.0")))
//        Espresso.onView(ViewMatchers.withId(R.id.rpi_high_text))
//            .check(ViewAssertions.matches(ViewMatchers.withSubstring("0.0")))
//        Espresso.onView(ViewMatchers.withId(R.id.rpi_low_text))
//            .check(ViewAssertions.matches(ViewMatchers.withSubstring("0.0")))
//        Espresso.onView(ViewMatchers.withId(R.id.gmpf_tax_years_text))
//            .check(ViewAssertions.matches(ViewMatchers.withSubstring("0.0")))
//        Espresso.onView(ViewMatchers.withId(R.id.gmpf_sixth_aprils_text))
//            .check(ViewAssertions.matches(ViewMatchers.withSubstring("0.0")))
//    }

}