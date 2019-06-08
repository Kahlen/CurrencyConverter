package com.currencyconverter.ui.data

data class CurrencyUpdatedModel(
    val items: List<CurrencyItemModel>,
    val updateFromRemote: Boolean
)