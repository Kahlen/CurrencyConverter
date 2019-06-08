package com.currencyconverter.ui.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class CurrencyItemModel(
    val currencyCode: String,
    @StringRes val currencyName: Int,
    val amount: Int,
    @DrawableRes val imageRes: Int,
    val editable: Boolean
)