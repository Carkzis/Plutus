package com.carkzis.android.plutus.util

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

/**
 * Extension function for displaying a snack bar depending on the message and length of
 * display time.
 */
fun View.showSnackbar(message: String, length: Int) {
    Snackbar.make(this, message, length).run {
        show()
    }
}

/**
 * Extension function for displaying a short toast.
 */
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).run {
        show()
    }
}