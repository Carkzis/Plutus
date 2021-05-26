package com.example.android.plutus

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@MediumTest
class ContentsFragmentTest {

    @Test
    fun clickPclsCalculatorButton_navigateToPclsFragment() {
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
            ContentsFragmentDirections.actionContentsFragmentToPclsFragment()
        )

    }

}