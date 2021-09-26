package com.carkzis.android.plutus.util

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carkzis.android.plutus.CpiItem
import com.carkzis.android.plutus.CpiPercentage
import com.carkzis.android.plutus.RpiItem
import com.carkzis.android.plutus.RpiPercentage
import com.carkzis.android.plutus.inflation.ApiLoadingStatus
import com.carkzis.android.plutus.inflation.*

/**
 * Binding adapter which alters the visibility of the progress bar depending on the status
 * of the network call.
 */
@BindingAdapter("loadingStatus")
fun bindLoadingStatus(statusProgressBar: ProgressBar, loadingStatus: ApiLoadingStatus) {
    when (loadingStatus) {
        ApiLoadingStatus.LOADING ->
            statusProgressBar.visibility = View.VISIBLE
        ApiLoadingStatus.ERROR, ApiLoadingStatus.DONE ->
            statusProgressBar.visibility = View.GONE
    }
}

/**
 * Binding adapter which alters the visibility of an error message depending on the status
 * of the network call.
 */
@BindingAdapter("errorMessage")
fun bindErrorMessage(errorMessage: TextView, loadingStatus: ApiLoadingStatus) {
    when (loadingStatus) {
        ApiLoadingStatus.LOADING, ApiLoadingStatus.DONE ->
            errorMessage.visibility = View.GONE
        ApiLoadingStatus.ERROR ->
            errorMessage.visibility = View.VISIBLE
    }
}