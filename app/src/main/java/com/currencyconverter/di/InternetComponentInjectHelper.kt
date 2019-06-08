package com.currencyconverter.di

import android.content.Context

object InternetComponentInjectHelper {
    fun provideInternetComponent(applicationContext: Context): InternetComponent {
        return if (applicationContext is InternetComponentProvider) {
            (applicationContext as InternetComponentProvider).provideInternetComponent()
        } else {
            throw IllegalStateException("The application context does not implement InternetComponentProvider.")
        }
    }
}