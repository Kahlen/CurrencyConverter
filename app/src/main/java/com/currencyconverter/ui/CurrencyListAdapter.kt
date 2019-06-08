package com.currencyconverter.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.currencyconverter.databinding.CurrencyItemBinding
import com.currencyconverter.ui.data.CurrencyItemModel

class CurrencyListAdapter(
    private val interactionListener: CurrencyItemInteractionListener
): ListAdapter<CurrencyItemModel, CurrencyViewHolder>(CurrencyDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        return CurrencyViewHolder(
            CurrencyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            interactionListener)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bindModel(getItem(position))
    }
}