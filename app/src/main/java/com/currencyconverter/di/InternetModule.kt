package com.currencyconverter.di

import com.currencyconverter.BuildConfig
import com.currencyconverter.repository.CurrencyApi
import com.currencyconverter.repository.CurrencyJsonAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class InternetModule {

    @Singleton
    @Provides
    fun provideLivePricingApi(): CurrencyApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(CurrencyJsonAdapter()).build()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CurrencyApi::class.java)
    }
}