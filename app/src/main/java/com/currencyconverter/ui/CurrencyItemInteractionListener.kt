package com.currencyconverter.ui

interface CurrencyItemInteractionListener {
    fun onItemClicked(position: Int)
    fun onAmountChanged(amount: Int)
}