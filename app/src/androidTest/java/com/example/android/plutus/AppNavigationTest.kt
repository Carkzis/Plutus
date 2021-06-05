package com.example.android.plutus

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class AppNavigationTest {

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Test
    fun contentsScreen_clickOnPclsButton_navigateToPclsCalcFragment() {
        // On the contents screen
        val scenario = launchFragmentInContainer<ContentsFragment>(Bundle())
        val navController = mock(NavController::class.java)

        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // Click on the pcls calculator button
        onView(withId(R.id.pcls_button))
            .perform(click())

        // Verify that we navigate to the pcls calculator screen
        verify(navController).navigate(
            ContentsFragmentDirections.actionContentsFragmentToPclsCalcFragment()
        )
    }

    @Test
    fun contentsScreen_clickOnDateButton_navigateToDateCalcFragment() {
        // On the contents screen
        val scenario = launchFragmentInContainer<ContentsFragment>(Bundle())
        val navController = mock(NavController::class.java)

        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // Click the date calculator button
        onView(withId(R.id.date_button))
            .perform(click())

        // Verify that we navigate to the pcls calculator screen
        verify(navController).navigate(
            ContentsFragmentDirections.actionContentsFragmentToDateCalcFragment()
        )
    }

    @Test
    fun contentsScreen_clickOnInflationButton_navigateToInflationMainFragment() {
        // On the contents screen
        val scenario = launchFragmentInContainer<ContentsFragment>(Bundle())
        val navController = mock(NavController::class.java)

        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // Click the date calculator button
        onView(withId(R.id.inflation_button))
            .perform(click())

        // Verify that we navigate to the pcls calculator screen
        verify(navController).navigate(
            ContentsFragmentDirections.actionContentsFragmentToInflationMainFragment()
        )
    }

    @Test
    fun pclsScreen_backButton() = runBlockingTest {
        // On the contents screen
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the pcls calculator button
        onView(withId(R.id.pcls_button))
            .perform(click())

        // Click the back button
        pressBack()

        // Confirm we end up at the contents screen
        onView(withId(R.id.pcls_button)).check(matches(isDisplayed()))
    }

    @Test
    fun dateScreen_backButton() = runBlockingTest {
        // On the contents screen
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the pcls calculator button
        onView(withId(R.id.date_button))
            .perform(click())

        // Click the back button
        pressBack()

        // Confirm we end up at the contents screen
        onView(withId(R.id.date_button)).check(matches(isDisplayed()))
    }

    @Test
    fun inflationScreen_backButton() = runBlockingTest {
        // On the contents screen
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the pcls calculator button
        onView(withId(R.id.inflation_button))
            .perform(click())

        // Click the back button
        pressBack()

        // Confirm we end up at the contents screen
        onView(withId(R.id.inflation_button)).check(matches(isDisplayed()))
    }

    @Test
    fun inflationScreen_clickOnCpiButton_navigateToCpiInflationFragment() {
        // On the contents screen
        val scenario = launchFragmentInContainer<InflationMainFragment>(Bundle())
        val navController = mock(NavController::class.java)

        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // Click the date calculator button
        onView(withId(R.id.cpi_button))
            .perform(click())

        // Verify that we navigate to the pcls calculator screen
        verify(navController).navigate(
            InflationMainFragmentDirections.actionInflationMainFragmentToCpiInflationFragment()
        )
    }

    @Test
    fun cpiInflationScreen_backButton() = runBlockingTest {
        // On the contents screen
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the pcls calculator button
        onView(withId(R.id.inflation_button))
            .perform(click())

        // Click on cpi button
        onView(withId(R.id.cpi_button))
            .perform(click())

        // Click the back button
        pressBack()

        // Confirm we end up at the contents screen
        onView(withId(R.id.cpi_button)).check(matches(isDisplayed()))
    }
}