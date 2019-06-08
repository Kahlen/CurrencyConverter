package com.currencyconverter.repository.data

data class CurrencyRateModel(
    val base: Currency,
    val rates: Map<Currency, Double>
)