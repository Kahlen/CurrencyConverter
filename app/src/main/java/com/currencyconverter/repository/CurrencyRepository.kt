package com.currencyconverter.repository

import com.currencyconverter.repository.data.Currency
import com.currencyconverter.repository.data.CurrencyAmountModel
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class CurrencyRepository @Inject constructor(private val currencyApi: CurrencyApi) {

    @Volatile
    var latestRate: Map<Currency, Double>? = null

    fun getRemoteRates(currencies: List<Currency>, amount: Int): Observable<List<CurrencyAmountModel>> {
        return currencyApi.getLatest(currencies[0].code)
            .map { currencyRateModel ->
                latestRate = mutableMapOf(currencyRateModel.base to 1.0).apply {
                    putAll(currencyRateModel.rates.map { Pair(it.key, it.value) })
                }

                currencies.map { currency ->
                    if (currency == currencyRateModel.base) {
                        CurrencyAmountModel(currency, amount)
                    } else {
                        CurrencyAmountModel(currency, (currencyRateModel.rates[currency]!!*amount).toInt())
                    }
                }
            }
    }

    fun getLocalRates(currencies: List<CurrencyAmountModel>, amount: Int): Single<List<CurrencyAmountModel>> {
        latestRate?.let { lastRate ->
            return Single.fromCallable {
                val base = lastRate[currencies[0].currency]!!
                currencies.mapIndexed { index, currencyAmountModel ->
                    if (index == 0) {
                        currencyAmountModel.copy(amount = amount)
                    } else {
                        CurrencyAmountModel(
                            currencyAmountModel.currency,
                            (amount*lastRate[currencyAmountModel.currency]!!/base).toInt())
                    }
                }
            }
        }

        return getRemoteRates(currencies.map { it.currency }, amount).firstOrError()
    }
}