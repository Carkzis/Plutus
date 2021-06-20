package com.example.android.plutus

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface InflationApiService {

    // TODO: May need adjusting, as the required information is nested.
    @GET("d7g7/mm23/data")
    suspend fun getCpiPctInformation() : NetworkInflationItemContainer

    @GET("d7bt/mm23/data")
    suspend fun getCpiItemInformation() : NetworkInflationItemContainer

    @GET("czbh/mm23/data")
    suspend fun getRpiPctInformation() : NetworkInflationItemContainer

    @GET("chaw/mm23/data")
    suspend fun getRpiItemInformation() : NetworkInflationItemContainer

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