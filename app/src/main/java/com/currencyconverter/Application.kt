package com.currencyconverter

import android.app.Application
import com.currencyconverter.di.DaggerInternetComponent
import com.currencyconverter.di.InternetComponent
import com.currencyconverter.di.InternetComponentProvider

class Application: Application(), InternetComponentProvider {
    private lateinit var internetComponent: InternetComponent

    override fun provideInternetComponent(): InternetComponent {
        if (!this::internetComponent.isInitialized) {
            internetComponent = DaggerInternetComponent.builder().build()
        }
        return internetComponent
    }
}