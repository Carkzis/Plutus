package com.carkzis.android.plutus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment for displaying the "About" information, telling the user about the app.
 */
@AndroidEntryPoint
class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment.
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

}