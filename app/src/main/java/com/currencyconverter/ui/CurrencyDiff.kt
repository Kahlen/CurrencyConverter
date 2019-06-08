package com.currencyconverter.ui

import androidx.recyclerview.widget.DiffUtil
import com.currencyconverter.ui.data.CurrencyItemModel

class CurrencyDiff: DiffUtil.ItemCallback<CurrencyItemModel>() {
    override fun areItemsTheSame(oldItem: CurrencyItemModel, newItem: CurrencyItemModel): Boolean {
        return oldItem.currencyCode == newItem.currencyCode
    }

    override fun areContentsTheSame(oldItem: CurrencyItemModel, newItem: CurrencyItemModel): Boolean {
        return oldItem == newItem
    }
}