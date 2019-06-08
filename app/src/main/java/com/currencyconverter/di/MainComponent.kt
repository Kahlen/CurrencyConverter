package com.currencyconverter.di

import com.currencyconverter.ui.MainActivity
import dagger.Component

@ActivityScope
@Component(modules = [MainModule::class], dependencies = [InternetComponent::class])
interface MainComponent {
    fun injectMainActivity(activity: MainActivity)
}