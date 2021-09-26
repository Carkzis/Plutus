package com.carkzis.android.plutus.inflation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carkzis.android.plutus.GmpFixedRevaluation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GmpFixedRevalViewModel @Inject constructor() : ViewModel() {

    /**
     * Due to the size of the data for GMP Fixed Revaluation, I will hardcode these (and couldn't
     * find it on the ONS API anyway). This displays the revaluation rate for different dates.
     */
    val gmpRevaluationRates = MutableLiveData<List<GmpFixedRevaluation>>().apply {
        value = mutableListOf(
            GmpFixedRevaluation("01/01/1900", "05/04/1988", "8.50%"),
            GmpFixedRevaluation("06/04/1988", "05/04/1993", "7.50%"),
            GmpFixedRevaluation("06/04/1993", "05/04/1997", "7.00%"),
            GmpFixedRevaluation("06/04/1997", "05/04/2002", "6.25%"),
            GmpFixedRevaluation("06/04/2002", "05/04/2007", "4.50%"),
            GmpFixedRevaluation("06/04/2007", "05/04/2012", "4.00%"),
            GmpFixedRevaluation("06/04/2012", "05/04/2017", "4.75%"),
            GmpFixedRevaluation("06/04/2017", "05/04/2022", "3.50%"),
        )
    }
}