package com.currencyconverter.repository

import com.currencyconverter.repository.data.Currency
import io.reactivex.Observable
import javax.inject.Inject

class CurrencyRepository @Inject constructor(private val currencyApi: CurrencyApi) {

    @Volatile
    var latestRate: Map<Currency, Double>? = null

    fun getAmounts(currency: Currency, amount: Int): Observable<Map<Currency, Int>> {
        return currencyApi.getLatest(currency.code)
            .map { currencyRateModel ->
                latestRate = mutableMapOf(currencyRateModel.base to 1.0).apply {
                    putAll(currencyRateModel.rates.map { Pair(it.key, it.value) })
                }
                latestRate?.let { map -> map.mapValues { rate -> (rate.value*amount).toInt() } }
            }
    }
}