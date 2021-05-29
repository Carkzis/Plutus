package com.example.android.plutus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.android.plutus.databinding.FragmentDateCalcBinding

class DateCalcFragment : Fragment() {

    private val viewModel by viewModels<DateCalcViewModel> {
        DateCalcViewModelFactory()
    }

    private lateinit var dataBinding: FragmentDateCalcBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_date_calc, container, false)
    }

}