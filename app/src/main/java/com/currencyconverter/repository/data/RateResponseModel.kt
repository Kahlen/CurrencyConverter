package com.currencyconverter.repository.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RateResponseModel(
    val base: String,
    val rates: Map<String, Double>
)