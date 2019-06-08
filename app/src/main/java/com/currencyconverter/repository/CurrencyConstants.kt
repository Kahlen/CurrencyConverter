package com.currencyconverter.repository

import com.currencyconverter.repository.data.Currency

object CurrencyConstants {
    val CURRENCY_MAP = Currency.values().associate { Pair(it.code, it) }
}