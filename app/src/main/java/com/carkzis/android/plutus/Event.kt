package com.carkzis.android.plutus

/**
 * This class takes in content, and ensures that it is only used once.
 * Returns null is hasBeenHandled is true, or the content if is false (it's default state).
 * Thank you to Jose Alcerreca for providing this (it is not my code!):
 * (https://gist.github.com/JoseAlcerreca/5b661f1800e1e654f07cc54fe87441af).
 */
open class Event <out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow read but not write

    // Returns the content and prevents its use again.
    fun getContextIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}