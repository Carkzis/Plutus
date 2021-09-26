package com.carkzis.android.plutus

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

/**
 * This is a retrofit service, which is used to obtain different forms of revaluation data
 * from the Office for National Statistics (ONS).
 */
interface InflationApiService {

    /**
     * Retrieves data relating to the CPI 12-month percentage changes.
     */
    @GET("d7g7/mm23/data")
    suspend fun getCpiPctInformation() : NetworkInflationItemContainer

    /**
     * Retrieves CPI item data.
     */
    @GET("d7bt/mm23/data")
    suspend fun getCpiItemInformation() : NetworkInflationItemContainer

    /**
     * Retrieves data relating to the RPI 12-month percentage changes.
     */
    @GET("czbh/mm23/data")
    suspend fun getRpiPctInformation() : NetworkInflationItemContainer

    /**
     * Retrieves RPI item data.
     */
    @GET("chaw/mm23/data")
    suspend fun getRpiItemInformation() : NetworkInflationItemContainer

}

/**
 * Configures retrofit to parse JSON data from the ONS, using the MoshiConverterFactory.
 */
private val retrofit = Retrofit.Builder()
    .baseUrl("https://www.ons.gov.uk/economy/inflationandpriceindices/timeseries/")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

/**
 * Entry point for accessing the network.
 */
object InflationRateApi {

    val retrofitService : InflationApiService by lazy {
        retrofit.create(InflationApiService::class.java)
    }

}