package com.currencyconverter.util

import com.currencyconverter.repository.CurrencyConstants.CURRENCY_MAP
import com.currencyconverter.repository.data.Currency
import com.currencyconverter.repository.data.CurrencyAmountModel
import com.currencyconverter.ui.data.CurrencyItemModel

fun <T> List<T>.bumpToTop(index: Int): List<T> {
    if (index <= 0 || index >= this.size) {
        return this
    }
    return listOf(this[index]) +
            this.subList(0, index) +
            if (index < this.size-1) {
                this.subList(index+1, this.size)
            } else emptyList()
}

fun CurrencyAmountModel.toCurrencyItemModel(editable: Boolean): CurrencyItemModel {
    return CurrencyItemModel(
        currencyCode = this.currency.code,
        currencyName = this.currency.nameRes,
        imageRes = this.currency.flagRes,
        amount = this.amount,
        editable = editable)
}

fun CurrencyItemModel.toCurrencyAmountModel(): CurrencyAmountModel {
    return CurrencyAmountModel(
        currency = CURRENCY_MAP[this.currencyCode]!!,
        amount = this.amount)
}

fun CurrencyItemModel.getCurrency(): Currency {
    return CURRENCY_MAP[this.currencyCode]!!
}