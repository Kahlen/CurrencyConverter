package com.currencyconverter.repository

import com.currencyconverter.repository.CurrencyConstants.CURRENCY_MAP
import com.currencyconverter.repository.data.CurrencyRateModel
import com.currencyconverter.repository.data.RateResponseModel
import com.squareup.moshi.FromJson

class CurrencyJsonAdapter {
    @FromJson
    fun fromJson(response: RateResponseModel): CurrencyRateModel {
        return CurrencyRateModel(
            CURRENCY_MAP[response.base]!!,
            response.rates.mapKeys { CURRENCY_MAP[it.key]!! }
        )
    }
}