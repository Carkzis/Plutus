package com.example.android.plutus

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface InflationApiService {

    // TODO: May need adjusting, as the required information is nested.
    @GET("d7g7/mm23/data.json")
    suspend fun getCpiInformation() : NetworkInflationRateContainer

    @GET("czbh/mm23/data.json")
    suspend fun getRpiInformation() : NetworkInflationRateContainer

}

private val retrofit = Retrofit.Builder()
    .baseUrl("https://www.ons.gov.uk/economy/inflationandpriceindices/timeseries/")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

object InflationRateApi {

    val retrofitService : InflationApiService by lazy {
        retrofit.create(InflationApiService::class.java)
    }

}