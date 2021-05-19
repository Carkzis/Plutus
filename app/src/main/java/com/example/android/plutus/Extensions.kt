package com.example.android.plutus

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar

/**
 * Extension functions.
 */

fun View.showSnackbar(message: String, length: Int) {
    Snackbar.make(this, message, length).run {
        show()
    }
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).run {
        show()
    }
}