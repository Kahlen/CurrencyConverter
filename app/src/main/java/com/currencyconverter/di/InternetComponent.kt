package com.currencyconverter.di

import com.currencyconverter.repository.CurrencyApi
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [InternetModule::class])
interface InternetComponent {
    fun provideCurrencyApi(): CurrencyApi
}