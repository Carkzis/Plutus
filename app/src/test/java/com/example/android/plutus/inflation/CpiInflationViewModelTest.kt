package com.example.android.plutus.inflation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.plutus.CpiInflationViewModel
import com.example.android.plutus.data.FakeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
class CpiInflationViewModelTest() {

    private lateinit var cpiInflationViewModel: CpiInflationViewModel

    private lateinit var inflationRepository: FakeRepository

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        inflationRepository = FakeRepository()
        cpiInflationViewModel = CpiInflationViewModel(inflationRepository)
    }



}