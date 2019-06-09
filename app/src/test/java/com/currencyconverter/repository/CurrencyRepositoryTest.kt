package com.currencyconverter.repository

import com.currencyconverter.repository.data.Currency
import com.currencyconverter.repository.data.CurrencyAmountModel
import com.currencyconverter.repository.data.CurrencyRateModel
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CurrencyRepositoryTest {

    @Mock
    private lateinit var currencyApi: CurrencyApi

    private lateinit var repository: CurrencyRepository

    @Before
    fun setUp() {
        whenever(currencyApi.getLatest(Currency.EUR.code)).thenReturn(Observable.just(
            CurrencyRateModel(
                base = Currency.EUR,
                rates = mapOf(Currency.GBP to 2.0, Currency.USD  to 3.0)
            )
        ))
        repository = CurrencyRepository(currencyApi)
    }

    @Test
    fun testGetRemoteRates() {
        val observer = TestObserver<List<CurrencyAmountModel>>()
        repository.getRemoteRates(listOf(Currency.EUR, Currency.GBP, Currency.USD), 100).subscribe(observer)
        observer.assertValueCount(1)
        observer.assertValueAt(0,
            listOf(CurrencyAmountModel(Currency.EUR, 100),
                CurrencyAmountModel(Currency.GBP, 200),
                CurrencyAmountModel(Currency.USD, 300))
        )
    }

    @Test
    fun testLocalRates() {
        repository.getRemoteRates(listOf(Currency.EUR, Currency.GBP, Currency.USD), 100).subscribe()

        val observer = TestObserver<List<CurrencyAmountModel>>()
        repository.getLocalRates(
            listOf(CurrencyAmountModel(Currency.EUR, 100),
                CurrencyAmountModel(Currency.GBP, 200),
                CurrencyAmountModel(Currency.USD, 300)),
            500)
            .subscribe(observer)
        observer.assertValueCount(1)
        observer.assertValueAt(0,
            listOf(CurrencyAmountModel(Currency.EUR, 500),
                CurrencyAmountModel(Currency.GBP, 1000),
                CurrencyAmountModel(Currency.USD, 1500)))
    }
}