package com.currencyconverter.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.currencyconverter.repository.CurrencyRepository
import com.currencyconverter.ui.CurrencyViewModel
import dagger.Module
import dagger.Provides
import java.lang.IllegalArgumentException

@Module
class MainModule {
    @Provides
    fun provideCurrencyViewModelFactory(
        repository: CurrencyRepository
    ): ViewModelProvider.Factory {
        return object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(CurrencyViewModel::class.java)) {
                    return CurrencyViewModel(repository) as T
                }

                throw IllegalArgumentException("Unknown view model type ${modelClass.simpleName}")
            }
        }
    }
}